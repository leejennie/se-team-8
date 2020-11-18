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
            int min = 9999;
            for(MapLocation loc: refineries) {
                int tmp = rc.getLocation().distanceSquaredTo(loc);
                if (tmp < min) {
                    currentGoal = loc;
                    min = tmp;
                }
            }
        }

        // If there are too


        // try to build based on the spawnFilter list
        int[] spawnFilter = {0, 0, 0, 0, 0};
        // Conditions to skip to certain buildings
        // if there's more than twice as many refineries as designSchools, don't build more refineries
        if(refineries.size() > fulCenters.size() * 2) // || (refineries.size() > 0 && designSchools.size() == 0)) --changed this just to get more things to spawn
            spawnFilter[0]++;
        // if there's more fulfillment centers than design schools, don't build fulfillment centers
        if(fulCenters.size() > designSchools.size() )
            spawnFilter[1]++;
        // if the current location is above a pollution threshold, prioritize a vaporator
        if(rc.sensePollution(rc.getLocation()) > 2000) {
            spawnFilter[0]++;
            spawnFilter[1]++;
            spawnFilter[2]++;
        }
        // if the enemy HQ location isn't known, skip design schools
        if(enemyHqLocation == null) {
            spawnFilter[2]++;
        }
        int buildingDistanceThreshold = 200;
        for(MapLocation bld: refineries) {
            if(bld.distanceSquaredTo(rc.getLocation()) < buildingDistanceThreshold)
                spawnFilter[0]++;
        }
        for(MapLocation bld: fulCenters) {
            if(bld.distanceSquaredTo(rc.getLocation()) < buildingDistanceThreshold)
                spawnFilter[1]++;
        }
        for(MapLocation bld: designSchools) {
            if(bld.distanceSquaredTo(rc.getLocation()) < buildingDistanceThreshold)
                spawnFilter[2]++;
        }
        for(MapLocation bld: vaporators) {
            if(bld.distanceSquaredTo(rc.getLocation()) < buildingDistanceThreshold)
                spawnFilter[3]++;
        }
        for(MapLocation bld: netGuns) {
            if(bld.distanceSquaredTo(rc.getLocation()) < buildingDistanceThreshold)
                spawnFilter[4]++;
        }
        for(int i = 0; i < spawnFilter.length; i++) {
            if (spawnFilter[i] < 1) {
                if(PlayerBot.tryBuild(spawnList[i])) {
                    System.out.println("Built a thing");
                }
            }
        }

        // check for nearby soupLocs
        int maxSoupDistance = 10000;
        if(soupLocs != null) for(MapLocation loc: soupLocs) {
            if(rc.getLocation().distanceSquaredTo(loc) < maxSoupDistance) {
                currentGoal = loc;
            }
        }

        // try to sense nearby soup and set it as the goal if there isn't already a soup goal
        MapLocation soup[] = rc.senseNearbySoup();
        if(currentGoal == null && soup.length > 0)
            currentGoal = soup[0];
        else if(soup.length > 0) {
            boolean found = false;
            for(int i = 0; i < soup.length; i++) {
                if(soup[i] == currentGoal) {
                    found = true;
                    break;
                }
            }
            if(!found)
                currentGoal = soup[0];
        }


        // try to mine
        skipMovement = false;
        for (Direction dir : Direction.allDirections()) {
            MapLocation tmp = rc.getLocation().add(dir);
            if (tryMine(dir)) {
                // Check if this is the first time mining here


                System.out.println("I mined soup! " + rc.getSoupCarrying());
                skipMovement = true;
                //if (!soupLocs.contains(tmp)) {
                    //soupLocs.add(tmp);
                    //Blockchain.sendSoupLoc(tmp, 10);
                //}
                break;
            }
            // if couldn't mine and loc is in goals, notify it is used
            //else {
                //if(!usedLocs.contains(tmp) && soupLocs.contains(tmp)) {
                    //Blockchain.sendStatusUpdate(UPD_SOUP_USED, new int[]{tmp.x, tmp.y}, 10);
                //}
            //}
        }

        endTurn();
    }
}
