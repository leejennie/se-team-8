package team8player;
import battlecode.common.*;

import java.util.*;

import static battlecode.common.GameConstants.BLOCKCHAIN_TRANSACTION_LENGTH;

public strictfp class RobotPlayer {
    static RobotController rc;

    static Direction[] directions = {
            Direction.NORTH,
            Direction.NORTHEAST,
            Direction.EAST,
            Direction.SOUTHEAST,
            Direction.SOUTH,
            Direction.SOUTHWEST,
            Direction.WEST,
            Direction.NORTHWEST
    };
    static RobotType[] spawnedByMiner = {RobotType.REFINERY, RobotType.VAPORATOR, RobotType.DESIGN_SCHOOL,
            RobotType.FULFILLMENT_CENTER, RobotType.NET_GUN};

    static int turnCount;
    static int lastAction = -1; // so we can know when a bot is doing something different on their current turn
    static int numMiners = 0;
    static MapLocation HqLocation;
    static int countDesignSchool = 0;
    static int countRefinery = 0;
    static LinkedList<MapLocation> goalLocs = new LinkedList<MapLocation>();
    static LinkedList<MapLocation> usedLocs = new LinkedList<MapLocation>();
    // Locations for enemy buildings
    static MapLocation enemyHqLocation;
    /*
     *
     * BLOCKCHAIN STUFF
     */
    static final int teamCode = 2662718; // Randomly generated number for id
    public enum message_type {
        ROBOT_LOCATION(0, "Robot Location"),
        SOUP_LOCATION(1, "Soup Location"),
        STATUS_UPDATE(2, "Status Update"),
        ETC(3, "Etc");
        private final int id;
        private final String desc;
        private message_type(int id, String desc) {
            this.id = id;
            this.desc = desc;
        }
    } // Add getters for easier string formatting
    public static String getMTDescFromId(int id) {
        for(final message_type mt: message_type.values()) {
            if (mt.id == id) {
                return mt.desc;
            }
        }
        return "";
    }


    public enum _robot_type { // added mainly to make code more readable
        _MINER(0, "Miner"),
        _LANDSCAPER(1, "Landscaper"),
        _DELIVERY_DRONE(2, "Delivery Drone"),
        _HQ(3, "HQ"),
        _REFINERY(4, "Refinery"),
        _VAPORATOR(5, "Vaporator"),
        _DESIGN_SCHOOL(6, "Design School"),
        _FULLFILLMENT_CENTER(7, "Fullfillment Center"),
        _NET_GUN(8, "Net Gun"),
        _COW(9, "Cow");
        private final int id;
        private final String desc;
        private _robot_type(int id, String desc) { this.id = id; this.desc = desc; }
    }
    public static String getRbtDescFromId(int id) {
        for(final _robot_type rbt: _robot_type.values()) {
            if (rbt.id == id) {
                return rbt.desc;
            }
        }
        return "";
    }

    public enum hostility {
        ALLY(0, "Ally"),
        ENEMY(1, "Enemy");
        private final int id;
        private final String desc;
        private hostility(int id, String desc) {
            this.id = id;
            this.desc = desc;
        }
    }
    public static String getHostilityFromId(int id) {
        for (final hostility hst : hostility.values()) {
            if (hst.id == id) {
                return hst.desc;
            }
        }
        return "";
    }

    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * If this method returns, the robot dies!
     **/
    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {

        // This is the RobotController object. You use it to perform actions from this robot,
        // and to get information on its current status.
        RobotPlayer.rc = rc;

        turnCount = 0;

        System.out.println("I'm a " + rc.getType() + " and I just got created!");
        while (true) {
            turnCount += 1;
            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode
            try {
                // Here, we've separated the controls into a different method for each RobotType.
                // You can add the missing ones or rewrite this into your own control structure.
                System.out.println("I'm a " + rc.getType() + "! Location " + rc.getLocation());
                // Try to fill the HQ Locations if either is unknown
                if(HqLocation == null || enemyHqLocation == null) {
                    //check the blockchain every n turns for either HQ location
                    if(rc.getRoundNum() % 3 == 0) {
                        if(HqLocation == null) {
                            int[] filter = {teamCode, message_type.ROBOT_LOCATION.id,
                                    _robot_type._HQ.id, hostility.ALLY.id, -1, -1, -1};
                            LinkedList<int[]> tmpa = getMessagesFromBC(1, filter);
                            int[] message = {0};
                            if(!tmpa.isEmpty()) { message = tmpa.get(0); }
                            if(message[0] == teamCode) { HqLocation = new MapLocation(message[4], message[5]); }
                            System.out.printf("I just updated the location an ally HQ from the Blockchain!%n");
                        }
                        if(enemyHqLocation == null) {
                            int[] filter = {teamCode, message_type.ROBOT_LOCATION.id,
                                    _robot_type._HQ.id, hostility.ENEMY.id, -1, -1, -1};
                            LinkedList<int[]> tmpe = getMessagesFromBC(1, filter);
                            int[] message = {0};
                            if(!tmpe.isEmpty()) { message = tmpe.get(0); }
                            if(message[0] == teamCode) { enemyHqLocation = new MapLocation(message[4], message[5]); }
                            System.out.printf("I just updated the location an enemy HQ from the Blockchain!%n");
                        }
                    }
                    findHQ();
                }
                switch (rc.getType()) {
                    case HQ:                 runHQ();                break;
                    case MINER:              runMiner();             break;
                    case REFINERY:           runRefinery();          break;
                    case VAPORATOR:          runVaporator();         break;
                    case DESIGN_SCHOOL:      runDesignSchool();      break;
                    case FULFILLMENT_CENTER: runFulfillmentCenter(); break;
                    case LANDSCAPER:         runLandscaper();        break;
                    case DELIVERY_DRONE:     runDeliveryDrone();     break;
                    case NET_GUN:            runNetGun();            break;
                }

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println(rc.getType() + " Exception");
                e.printStackTrace();
            }
        }
    }

    /*  Taken from https://www.youtube.com/watch?v=YJjs7Eo6IrU&ab_channel=MITBattlecode
     *  to make it so all robots will check the HQ location
     */
    static void findHQ() throws GameActionException {
        if (HqLocation == null) {
            RobotInfo[] robots = rc.senseNearbyRobots();
            for (RobotInfo robot : robots) {
                // if(robot.type == RobotType.HQ && robot.team == rc.getTeam()) {
                if(robot.type == RobotType.HQ) {
                    if(robot.team == rc.getTeam()) {
                        HqLocation = robot.location;
                        sendRobotLoc(robot.location, _robot_type._HQ.id, 0, 10);
                    }
                    else {
                        enemyHqLocation = robot.location;
                        sendRobotLoc(enemyHqLocation, _robot_type._HQ.id, 1, 10);
                    }
                }
            }
        }
    }

    static void runHQ() throws GameActionException {
        //Taken from https://www.youtube.com/watch?v=B0dYT3KZd9Y lecture video. Liked the way they produced miners and
        // thought it was helpful to winning the game because having more miners can produce more "robots"
        if(numMiners < 15) {
            for (Direction dir : directions) {
                if (tryBuild(RobotType.MINER, dir)) {
                    numMiners++;
                    System.out.println("Miner created!");
                }
            }
        }
    }

    static void runMiner() throws GameActionException {
        //tryBlockchain();
        // If last action wasn't mining, update goalLocs every n turns


        // tryBuild(randomSpawnedByMiner(), randomDirection());
        for (Direction dir : directions)
            if (tryMine(dir)) {
                // Check if this is the first time mining here
                if(lastAction != 0) {
                    MapLocation loc = rc.getLocation();
                        // Check if we have put this location on the blockchain and send it if not
                    int[] filter = {teamCode, message_type.SOUP_LOCATION.id, loc.x, loc.y, -1, -1, -1};
                    LinkedList<int[]> tmp = getMessagesFromBC(1, filter);
                    if(!tmp.isEmpty()) {
                        if(tmp.get(0)[0] != teamCode) {
                            sendSoupLoc(loc, 10);
                        }
                    }
                }
                System.out.println("I mined soup! " + rc.getSoupCarrying());
                lastAction = 0;
            }
            else {
                if(lastAction == 0) { // Add the location to used up goalLocs
                    MapLocation loc= rc.getLocation();
                    goalLocs.remove(loc);
                    usedLocs.add(loc);
                }
            }
        for (Direction dir : directions)
            if (tryRefine(dir)) {
                System.out.println("I refined soup! " + rc.getTeamSoup());
                lastAction = 1;
            }
        for (Direction dir : directions)
            if(tryBuild(RobotType.DESIGN_SCHOOL, dir)) {
                countDesignSchool++;
                System.out.println("Design school created");
                lastAction = 2;
            }
        for (Direction dir : directions)
            if(tryBuild(RobotType.LANDSCAPER, dir)) {
                System.out.println("Landscaper created");
                lastAction = 3;
            }
        if (rc.getSoupCarrying() == RobotType.MINER.soupLimit) {
            Direction toHQ = rc.getLocation().directionTo(HqLocation);
            if(tryMove(toHQ)) {
                System.out.println("move to HQ");
                lastAction = 4;
            }
        }
        tryMove(randomDirection());
        if (tryMove(randomDirection())) {
            System.out.println("I moved!");
            lastAction = 4;
        }
        for (Direction dir : directions) {
            tryBuild(RobotType.FULFILLMENT_CENTER, dir);
            lastAction = 5;
        }
    }

    static void runRefinery() throws GameActionException {
        // System.out.println("Pollution: " + rc.sensePollution(rc.getLocation()));
    }

    static void runVaporator() throws GameActionException {

    }

    static void runDesignSchool() throws GameActionException {

    }

    static void runFulfillmentCenter() throws GameActionException {
        for (Direction dir : directions)
            tryBuild(RobotType.DELIVERY_DRONE, dir);
    }

    static void runLandscaper() throws GameActionException {

        int currDirt = rc.getDirtCarrying();
        if (currDirt == 0) {
            Direction dir = randomDirection();
            if (tryDig(dir)) {
                System.out.println("I dug in the " + dir + " direction.");
                return;
            }
        }
        else if (currDirt == 25) {
                Direction dir = randomDirection();
                if (tryDepositDirt(dir)) {
                    System.out.println("I deposited dirt in the " + dir + " direction.");
                    return;
                }
            }

        else {
            for (Direction dir : directions)
                if (tryDig(dir)) {
                    System.out.println("I dug in the " + dir + " direction.");
                    return;
                }
            for (Direction dir : directions)
                if (tryDepositDirt(dir)){
                    System.out.println("I deposited dirt in the " + dir + " direction.");
                    return;
                }
        }
        // If no other actions can be performed, try to move
        tryMove();

    }

    static void runDeliveryDrone() throws GameActionException {
        Team enemy = rc.getTeam().opponent();
        if (!rc.isCurrentlyHoldingUnit()) {
            // See if there are any enemy robots within capturing range
            RobotInfo[] robots = rc.senseNearbyRobots(GameConstants.DELIVERY_DRONE_PICKUP_RADIUS_SQUARED, enemy);

            if (robots.length > 0) {
                // Pick up a first robot within range
                rc.pickUpUnit(robots[0].getID());
                System.out.println("I picked up " + robots[0].getID() + "!");
            }
        } else {
            // No close robots, so search for robots within sight radius
            tryMove(randomDirection());
        }
    }

    static void runNetGun() throws GameActionException {

    }

    /**
     * Returns a random Direction.
     *
     * @return a random Direction
     */
    static Direction randomDirection() {
        return directions[(int) (Math.random() * directions.length)];
    }

    /**
     * Returns a random RobotType spawned by miners.
     *
     * @return a random RobotType
     */
    static RobotType randomSpawnedByMiner() {
        return spawnedByMiner[(int) (Math.random() * spawnedByMiner.length)];
    }

    static boolean tryMove() throws GameActionException {
        for (Direction dir : directions)
            if (tryMove(dir))
                return true;
        return false;
        // MapLocation loc = rc.getLocation();
        // if (loc.x < 10 && loc.x < loc.y)
        //     return tryMove(Direction.EAST);
        // else if (loc.x < 10)
        //     return tryMove(Direction.SOUTH);
        // else if (loc.x > loc.y)
        //     return tryMove(Direction.WEST);
        // else
        //     return tryMove(Direction.NORTH);
    }

    /**
     * Attempts to move in a given direction.
     *
     * @param dir The intended direction of movement
     * @return true if a move was performed
     * @throws GameActionException
     */
    static boolean tryMove(Direction dir) throws GameActionException {
        // System.out.println("I am trying to move " + dir + "; " + rc.isReady() + " " + rc.getCooldownTurns() + " " + rc.canMove(dir));
        if (rc.isReady() && rc.canMove(dir)) {
            rc.move(dir);
            return true;
        } else return false;
    }

    /**
     * Attempts to dig in a given direction
     *
     * @param dir The intended direction to dig
     * @return true if it dug
     * @throws GameActionException
     */
    static boolean tryDig(Direction dir) throws GameActionException {
        if (rc.canDigDirt(dir)) {
            rc.digDirt(dir);
            return true;
        }
        return false;
    }

    /**
     * Attempts to deposit dirt in a given direction
     * 
     * @param dir The intended direction to deposit
     * @return true if deposited
     * @throws GameActionException
     */
    static boolean tryDepositDirt(Direction dir) throws GameActionException {
        if(rc.canDepositDirt(dir)) {
            rc.depositDirt(dir);
            return true;
        }
        return false;
    }

    /**

     * Attempts to build a given robot in a given direction.
     *
     * @param type The type of the robot to build
     * @param dir The intended direction of movement
     * @return true if a move was performed
     * @throws GameActionException
     */
    static boolean tryBuild(RobotType type, Direction dir) throws GameActionException {
        if (rc.isReady() && rc.canBuildRobot(type, dir)) {
            rc.buildRobot(type, dir);
            return true;
        } else return false;
    }

    /**
     * Attempts to mine soup in a given direction.
     *
     * @param dir The intended direction of mining
     * @return true if a move was performed
     * @throws GameActionException
     */
    static boolean tryMine(Direction dir) throws GameActionException {
        if (rc.isReady() && rc.canMineSoup(dir)) {
            rc.mineSoup(dir);
            return true;
        }
        return false;
    }

    /**
     * Attempts to refine soup in a given direction.
     * /
     *
     * @param dir The intended direction of refining
     * @return true if a move was performed
     * @throws GameActionException
     */
    static boolean tryRefine(Direction dir) throws GameActionException {
        if (rc.isReady() && rc.canDepositSoup(dir)) {
            rc.depositSoup(dir, rc.getSoupCarrying());
            return true;
        } else return false;
    }

    // Takes a filter for messages and returns the first matching message with -1 representing a wildcard
    // Ex to get enemy HQ Location:
    // getMessageFromBlockchain([teamCode, message_type.HQ_LOCATION.id, _robot_type._HQ.id, 1, -1, -1, -1]);
    public static LinkedList<int[]> getMessagesFromBC(int n, int[] msgFilter) throws GameActionException {
        int count = 0;
        LinkedList<int[]> result = new LinkedList<int[]>() {};
        for(int i = 1; i < rc.getRoundNum(); i++) {
            for(Transaction t : rc.getBlock(i)) {
                int[] message = t.getMessage();
                for(int j = 0; j < BLOCKCHAIN_TRANSACTION_LENGTH; j++) {
                    if(msgFilter[j] != -1 && msgFilter[j] == message[j]) {
                        if(j == BLOCKCHAIN_TRANSACTION_LENGTH - 1) {
                            System.out.printf("I received a %s message from the Blockchain!%n",
                                    getMTDescFromId(msgFilter[1]));
                            result.add(message);
                            count++;
                            if(count == n) { return result;}
                        }
                    }
                }
            }
        }
        return result;
    }

    public static void getGoalLocs(int robotType) throws GameActionException{
        goalLocs = new LinkedList<>();
        switch (robotType) {
            // _robot_type._MINER.id is a constant, but can't be used in this switch for some reason???
            // todo: add other robot types
            case 0:
                int[] filter = {teamCode, message_type.SOUP_LOCATION.id, -1, -1, -1, -1, -1};
                LinkedList<int[]> minerTmp = getMessagesFromBC(-1, filter);
                if(minerTmp.get(0)[0] == teamCode) {
                    for(int[] m: minerTmp) {
                        MapLocation tmpLoc = new MapLocation(m[2], m[3]);
                        if(!usedLocs.contains(tmpLoc)) {
                            goalLocs.add(tmpLoc);
                        }
                    }
                }
        }
        System.out.println("I just updated my goal locations!");
    }

    // Message senders
    public static void sendRobotLoc(MapLocation loc, int robotType, int hostile, int txCost)
            throws GameActionException {
        int[] message = new int[BLOCKCHAIN_TRANSACTION_LENGTH];
        message[0] = teamCode;
        message[1] = message_type.ROBOT_LOCATION.id;
        message[2] = robotType;
        message[3] = hostile; // 0 for ally, 1 for enemy robot
        message[4] = loc.x;
        message[5] = loc.y;
        if(rc.canSubmitTransaction(message, txCost)) {
            rc.submitTransaction(message, txCost);
            System.out.printf("I just sent the location of a %s %s at [%d, %d] to the blockchain for %d%n",
                    getHostilityFromId(hostile),
                    getRbtDescFromId(robotType),
                    loc.x,
                    loc.y,
                    txCost);
        }
    }

    public static void sendSoupLoc(MapLocation loc, int txCost) throws GameActionException {
        int[] message = new int[BLOCKCHAIN_TRANSACTION_LENGTH];
        message[0] = teamCode;
        message[1] = message_type.SOUP_LOCATION.id;
        message[2] = loc.x;
        message[3] = loc.y;
        if(rc.canSubmitTransaction(message, txCost)) {
            rc.submitTransaction(message, txCost);
            System.out.printf("I sent the location of soup at [%d, %d] to the blockchain for %d!%n",
                    loc.x,
                    loc.y,
                    txCost);
        }
    }

    public enum update_type {
        POLLUTION_LEVEL(0);
        private int id;
        private update_type(int id) {
            this.id = id;
        }
    }
     // Made as a pseudo template for future types of messages.
    public static void sendStatusUpdate(int updateType, int[] values, int txCost) throws GameActionException {
        int[] message = new int[BLOCKCHAIN_TRANSACTION_LENGTH];
        message[0] = teamCode;
        message[1] = message_type.STATUS_UPDATE.id;
        message[2] = updateType;
        int i = 3;
        for(int x: values) {
            message[i] = x;
            i++;
            if(i == BLOCKCHAIN_TRANSACTION_LENGTH)
                break;
        }
        if(rc.canSubmitTransaction(message, txCost)) {
            rc.submitTransaction(message, txCost);
            System.out.printf("I sent a %s update to the blockchain for %d!%n");
        }
    }


    // Sabotage methods
    public static int getEnemyBCIdentifier() throws GameActionException {
        // Build a hashmap of all the ints from the blockchain
        HashMap<Integer, Integer> collisionCounter = new HashMap<Integer, Integer>();
        for(int i = 0; i < rc.getRoundNum(); i++) {
            for(Transaction tx : rc.getBlock(i)) {
                int[] message = tx.getMessage();
                for(int j: message) {
                    Integer count = collisionCounter.get(j);
                    collisionCounter.put(j, count != null ? count+1 : 1);
                }
            }
        }
        // Get max value to find key, which is likely the enemy's message identifier
        return Collections.max(collisionCounter.entrySet(),
                    new Comparator<Map.Entry<Integer, Integer>>() {
                        @Override
                        public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                            return o1.getValue().compareTo(o2.getValue());
                        }
                    }
            ).getKey();
    }

    public static void sendJunkMessages(int enemyId) throws GameActionException{
        int[] message = new int[BLOCKCHAIN_TRANSACTION_LENGTH];
        message[0] = enemyId;
        for(int i = 1; i < BLOCKCHAIN_TRANSACTION_LENGTH; i++) {
            Random rand = new Random();
            message[i] = rand.nextInt(9999999);
        }
        if(rc.canSubmitTransaction(message, 10))
            rc.submitTransaction(message, 10);
    }


    static void tryBlockchain() throws GameActionException {
        if (turnCount < 3) {
            int[] message = new int[7];
            for (int i = 0; i < 7; i++) {
                message[i] = 123;
            }
            if (rc.canSubmitTransaction(message, 10))
                rc.submitTransaction(message, 10);
        }
        // System.out.println(rc.getRoundMessages(turnCount-1));
    }
}
