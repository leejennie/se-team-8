package team8player.Robots;

import battlecode.common.*;
import battlecode.server.GameState;
import team8player.*;
import static team8player.Globals.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

public class Miner extends Unit {
    static final RobotType[] spawnList = {RobotType.REFINERY, RobotType.FULFILLMENT_CENTER, RobotType.DESIGN_SCHOOL,
            RobotType.VAPORATOR, RobotType.NET_GUN};

    boolean waiting = false;

    /**
     * Robot constructor
     * @return a Miner
     */
    public Miner(RobotController rc) {
        super(rc);
    }

    /**
     * Returns a random RobotType spawned by miners.
     *
     * @return a random RobotType
     */
    static RobotType randomSpawnable() {
        return spawnList[(int) (Math.random() * spawnList.length)];
    }


    /**
     * Attempts to mine soup in a given direction.
     *
     * @param dir The intended direction of mining
     * @return true if a move was performed
     * @throws GameActionException
     */
    public static boolean tryMine(Direction dir) throws GameActionException {
        if (rc.isReady() && rc.canMineSoup(dir)) {
            rc.mineSoup(dir);
            return true;
        }
        return false;
    }

    /**
     * Attempts to deposit soup in a given direction.
     *
     * @param dir The intended direction of depositing
     * @return true if a move was performed
     * @throws GameActionException
     */
    public static boolean tryDeposit(Direction dir) throws GameActionException {
        if (rc.isReady() && rc.canDepositSoup(dir)) {
            rc.depositSoup(dir, 200);
            return true;
        }
        return false;
    }


    // separating the strategy phases into methods for readability
    public static void expandPhase() throws GameActionException {
        // If there are too many miners nearby, move away
        int count = 0;
        int x = currentLoc.x;
        int y = currentLoc.y;
        for(Integer[] miner: miners) {
                MapLocation tmp = new MapLocation(miner[2], miner[3]);
                if(currentLoc.distanceSquaredTo(tmp) < MINER_RADIUS) {
                    x = (miner[2] + x);
                    y = (miner[3] + y);
                    count++;
            }
        }
        if(count > 2) {
            x = x / count;
            y = y / count;
            currentGoal = null;
            MapLocation avg = new MapLocation(x, y);
            currDirection = rc.getLocation().directionTo(avg).opposite();
            // make sure it doesn't get stuck in a loop in place
            if(currDirection == Direction.CENTER) {
                currDirection = Direction.cardinalDirections()[(int)(Math.random() *
                        Direction.cardinalDirections().length)];
            }
            dirTurnsLeft = 15;
            while(rc.isReady())
                tryMove(currDirection);
        }

        // try to build based on the spawnFilter list
        int[] spawnFilter = {0, 0, 0, 0, 0};
        // Conditions to skip to certain buildings
        // Until there's a few refineries, skip other buildings
        if(refineries.size() < 2)
            for(int i = 1; i < spawnFilter.length; i++) { spawnFilter[i]++; }
        // if there's more than twice as many refineries as designSchools, don't build more refineries
        if(refineries.size() == 2)
            spawnFilter[0]++;
        // if there's more fulfillment centers than design schools, don't build fulfillment centers
        if(fulCenters.size() == 1)
            spawnFilter[1]++;
        if(designSchools.size() == 1)
            spawnFilter[2]++;
        if(vaporators.size() > netGuns.size())
            spawnFilter[3]++;
        // if the current location is above a pollution threshold, prioritize a vaporator
        if(rc.sensePollution(rc.getLocation()) > 50 && refineries.size() > 1) {
            spawnFilter[0]++;
            spawnFilter[1]++;
            spawnFilter[2]++;
        }

        // if the enemy HQ location isn't known, skip design schools
        //if(enemyHqLocation == null) { spawnFilter[2]++; }
        int buildingDistanceThreshold = 50;
        for(MapLocation bld: refineries) {
            if(bld.distanceSquaredTo(currentLoc) < buildingDistanceThreshold)
                spawnFilter[0]++;
        }
        for(MapLocation bld: fulCenters) {
            if(bld.distanceSquaredTo(currentLoc) < buildingDistanceThreshold)
                spawnFilter[1]++;
        }
        for(MapLocation bld: designSchools) {
            if(bld.distanceSquaredTo(currentLoc) < buildingDistanceThreshold)
                spawnFilter[2]++;
        }
        for(MapLocation bld: vaporators) {
            if(bld.distanceSquaredTo(currentLoc) < buildingDistanceThreshold)
                spawnFilter[3]++;
        }
        for(MapLocation bld: netGuns) {
            if(bld.distanceSquaredTo(currentLoc) < buildingDistanceThreshold)
                spawnFilter[4]++;
        }

        // Try to build
        for(RobotInfo rbt: nearbyBots) {
            if(rbt.type == RobotType.DELIVERY_DRONE && rbt.team != rc.getTeam())
                tryBuild(RobotType.NET_GUN);
        }
        for(int i = 0; i < spawnFilter.length; i++) {
            if (spawnFilter[i] < 1) {
                if(i == 1)
                tryBuild(spawnList[i]);
                break;
            }
        }
    }

    public static void searchPhase() {

    }

    public static void destroyPhase() {

    }


    @Override
    public void run() throws GameActionException {
        // Call the parent's run method to start the turn
        super.run();

        // if mining more would cause waste, try to deposit soup
        if(rc.getSoupCarrying() > RobotType.MINER.soupLimit - GameConstants.SOUP_MINING_RATE) {
            // try to deposit
            for(Direction dir: Direction.allDirections()) {
                tryDeposit(dir);
            }

            // if
            int min = 9999;
            for(MapLocation loc: refineries) {
                int tmp = rc.getLocation().distanceSquaredTo(loc);
                if (tmp < min) {
                    currentGoal = loc;
                    min = tmp;
                }
            }
        }

        switch(stratPhase) {
            case STR_PHS_EXPAND:
                expandPhase();
                break;
            case STR_PHS_SEARCH:
                searchPhase();
                break;
            case STR_PHS_DESTROY:
                destroyPhase();
                break;
            default:
                break;
        }

        // Things for the miner to do regardless of phase
        // check for nearby soupLocs
        int maxSoupDistance = 5;
        if(soupLocs != null && rc.isReady()) for(MapLocation loc: soupLocs) {
            if(rc.getLocation().distanceSquaredTo(loc) < maxSoupDistance) {
                currentGoal = loc;
            }
        }

        // try to sense nearby soup and set it as the goal if there isn't already a soup goal
        MapLocation soup[] = rc.senseNearbySoup();
        if(currentGoal == null && soup.length > 0 && rc.isReady())
            currentGoal = soup[(int)(Math.random() * soup.length)];
        else if(soup.length > 0 && rc.isReady()) {
            boolean found = false;
            for (MapLocation mapLocation : soup) {
                if (mapLocation == currentGoal) {
                    found = true;
                    break;
                }
            }
            if(!found)
                currentGoal = soup[(int)(Math.random() * soup.length)];
        }


        // try to mine
        skipMovement = false;
        for (Direction dir : Direction.allDirections()) {
            if (tryMine(dir)) {
                skipMovement = true;
                break;
            }
        }

        endTurn();
    }
}
