package team8player.Robots;

import battlecode.common.*;
import battlecode.server.GameState;
import team8player.*;
import static team8player.Globals.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

public class Miner extends Unit {
    static final RobotType[] spawnList = {RobotType.REFINERY, RobotType.DESIGN_SCHOOL, RobotType.FULFILLMENT_CENTER,
            RobotType.VAPORATOR, RobotType.NET_GUN};

    /**
     * Robot constructor
     * @return a Miner
     */
    public Miner() {
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
    static boolean tryMine(Direction dir) throws GameActionException {
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
    static boolean tryDeposit(Direction dir) throws GameActionException {
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
        // try to build based on the spawnFilter list
        int[] spawnFilter = {0, 0, 0, 0, 0};
        for (RobotInfo rbt : nearbyBots) {
            // if any of the bots are an enemy drone, try to build a net gun
            if(rbt.type == RobotType.DELIVERY_DRONE && rbt.team != rc.getTeam())
                for(int i = 0; i < 3; i++)
                    spawnFilter[i]++;
            switch(rbt.type) {
                case HQ:
                    spawnFilter[0]++;
                case REFINERY:
                    spawnFilter[0]++;
                    break;
                case DESIGN_SCHOOL:
                    spawnFilter[1]++;
                    break;
                case FULFILLMENT_CENTER:
                    spawnFilter[2]++;
                    break;
                case VAPORATOR:
                    spawnFilter[3]++;
                    break;
                case NET_GUN:
                    spawnFilter[4]++;

            }
        }
        // Conditions to skip to certain buildings
        // if there's more than twice as many refineries as designSchools, don't build more refineries
        if(refineries.size() > designSchools.size() * 2 || (refineries.size() > 0 && designSchools.size() == 0))
            spawnFilter[0]++;
        // if there's more design schools than fulfillment centers, don't build design schools
        if(designSchools.size() > fulCenters.size())
            spawnFilter[1]++;
        // if the current location is above a pollution threshold, prioritize a vaporator
        if(rc.sensePollution(rc.getLocation()) > 20) {
            spawnFilter[0]++;
            spawnFilter[1]++;
        }
        for(int i = 0; i < spawnFilter.length; i++) {
            if (spawnFilter[i] == 0) {
                for (Direction dir : Direction.allDirections()) {
                    if (PlayerBot.tryBuild(spawnList[i], dir)) {
                        MapLocation loc = rc.getLocation().add(dir);
                        Blockchain.sendStatusUpdate(MSG_STATUS_UPDATE,
                                new int[]{UPD_RBT_BUILT, BLD_DESIGNSCH,
                                        loc.x, loc.y},
                                10);
                    }
                }
                break; //break out of loop because if one building could not be build, none of them can
            }
        }

        // check for nearby soupLocs
        int maxSoupDistance = 100;
        if(soupLocs != null) for(MapLocation loc: soupLocs) {
            if(rc.getLocation().distanceSquaredTo(loc) < maxSoupDistance) {
                currentGoal = loc;
            }
        }
        endTurn();
    }
}
