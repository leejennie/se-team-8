package team8player;

import battlecode.common.*;
import static team8player.Globals.*;

import java.util.*;

import static battlecode.common.GameConstants.BLOCKCHAIN_TRANSACTION_LENGTH;





public class Blockchain {
    static RobotController rc;


    /**
     * Attempts to move in a given direction.
     *
     * @param rc
     * @return creates a blockchain object for the player to control it
     */
    public Blockchain(RobotController rc) {
        Blockchain.rc = rc;
    }

    // Takes a filter for messages and returns the first matching message with -1 representing a wildcard
    // Ex to get enemy HQ Location:
    // getMessageFromBlockchain([teamCode, message_type.HQ_LOCATION.id, _robot_type._HQ.id, 1, -1, -1, -1]);
    public static LinkedList<int[]> getMessages(int n, int[] msgFilter) throws GameActionException {
        int count = 0;
        LinkedList<int[]> result = new LinkedList<int[]>() {};
        for(int i = 1; i < rc.getRoundNum(); i++) {
            for(Transaction t : rc.getBlock(i)) {
                int[] message = t.getMessage();
                for(int j = 0; j < BLOCKCHAIN_TRANSACTION_LENGTH; j++) {
                    if(msgFilter[j] != -1 && msgFilter[j] == message[j]) {
                        if(j == BLOCKCHAIN_TRANSACTION_LENGTH - 1) {
                            //System.out.printf("I received a %s message from the Blockchain!%n",
                                    //getMTDescFromId(msgFilter[1]));
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

    public static void getGoalLocs(int robotType) throws GameActionException {
        goalLocs = new LinkedList<MapLocation>();
        switch (robotType) {
            // _robot_type._MINER.id is a constant, but can't be used in this switch for some reason???
            // todo: add other robot types
            case 0:
                int[] filter = {teamCode, MSG_SOUP_LOCATION, -1, -1, -1, -1, -1};
                LinkedList<int[]> minerTmp = getMessages(-1, filter);
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
    public static void sendRobotLoc(MapLocation loc, int robotType, int hostile, int txCost) throws GameActionException {
        int[] message = new int[BLOCKCHAIN_TRANSACTION_LENGTH];
        message[0] = teamCode;
        message[1] = MSG_ROBOT_LOCATON;
        message[2] = robotType;
        message[3] = hostile; // 0 for ally, 1 for enemy robot
        message[4] = loc.x;
        message[5] = loc.y;
        if(rc.canSubmitTransaction(message, txCost)) {
            rc.submitTransaction(message, txCost);
            //System.out.printf("I just sent the location of a %s %s at [%d, %d] to the blockchain for %d%n",
              //      getHostilityFromId(hostile),
              //      getRbtDescFromId(robotType),
              //      loc.x,
              //      loc.y,
              //      txCost);
        }
    }

    public static void sendSoupLoc(MapLocation loc, int txCost) throws GameActionException {
        int[] message = new int[BLOCKCHAIN_TRANSACTION_LENGTH];
        message[0] = teamCode;
        message[1] = MSG_SOUP_LOCATION;
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
    // Made as a pseudo template for future types of messages.
    public static void sendStatusUpdate(int updateType, int[] values, int txCost) throws GameActionException {
        int[] message = new int[BLOCKCHAIN_TRANSACTION_LENGTH];
        message[0] = teamCode;
        message[1] = MSG_STATUS_UPDATE;
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
