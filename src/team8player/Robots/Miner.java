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

        // If there are too many miners nearby, move away -- removed because it wasn't working
        /*int x = rc.getLocation().x;
        int y = rc.getLocation().y;
        int count = 0;
        for(RobotInfo rbt: nearbyBots) {
            if(rbt.type == RobotType.MINER) {
                x = (rbt.location.x + x) / 2;
                y = (rbt.location.y + y) / 2;
                count++;
            }
        }
        if(count > 2) {
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
        }*/

        // try to build based on the spawnFilter list
        int[] spawnFilter = {0, 0, 0, 0, 0};
        // Conditions to skip to certain buildings
        // if there's more than twice as many refineries as designSchools, don't build more refineries
        if(refineries.size() > fulCenters.size() + 1) // || (refineries.size() > 0 && designSchools.size() == 0)) --changed this just to get more things to spawn
            spawnFilter[0]++;
        // if there's more fulfillment centers than design schools, don't build fulfillment centers
        if(fulCenters.size() > designSchools.size())
            spawnFilter[1]++;
        if(designSchools.size() > vaporators.size())
            spawnFilter[2]++;
        if(vaporators.size() > netGuns.size())
            spawnFilter[3]++;
        // if the current location is above a pollution threshold, prioritize a vaporator
        if(rc.sensePollution(rc.getLocation()) > 20) {
            spawnFilter[0]++;
            spawnFilter[1]++;
            spawnFilter[2]++;
        }
        // if the enemy HQ location isn't known, skip design schools
        //if(enemyHqLocation == null) { spawnFilter[2]++; }
        int buildingDistanceThreshold = 10;
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

        // Try to build
        for(RobotInfo rbt: nearbyBots) {
            if(rbt.type == RobotType.DELIVERY_DRONE && rbt.team != rc.getTeam())
                tryBuild(RobotType.NET_GUN);
        }
        spawnFilter[0] = 0;
        for(int i = 0; i < spawnFilter.length; i++) {
            if (spawnFilter[i] < 1) {
                tryBuild(spawnList[i]);
            }
        }

        // check for nearby soupLocs
        int maxSoupDistance = 5;
        if(soupLocs != null) for(MapLocation loc: soupLocs) {
            if(rc.getLocation().distanceSquaredTo(loc) < maxSoupDistance) {
                currentGoal = loc;
            }
        }

        // try to sense nearby soup and set it as the goal if there isn't already a soup goal
        MapLocation soup[] = rc.senseNearbySoup();
        if(currentGoal == null && soup.length > 0)
            currentGoal = soup[(int)(Math.random() * soup.length)];
        else if(soup.length > 0) {
            boolean found = false;
            for(int i = 0; i < soup.length; i++) {
                if(soup[i] == currentGoal) {
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
            MapLocation tmp = rc.getLocation().add(dir);
            if (tryMine(dir)) {
                skipMovement = true;
                break;
            }
        }

        endTurn();
    }
}
