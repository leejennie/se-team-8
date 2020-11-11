package team8player.Robots;

import battlecode.common.*;
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
            // try to refine
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
        // if there isn't a design school nearby, try to build one.
        int[] spawnFilter = {0, 0, 0, 0, 0};
        for (RobotInfo rbt : nearbyBots) {
            switch(rbt.type) {
                case REFINERY:
                    spawnFilter[0] += 1;
                    break;
                case DESIGN_SCHOOL:
                    spawnFilter[1] += 1;
                    break;
                case FULFILLMENT_CENTER:
                    spawnFilter[2] += 1;
                    break;
                case VAPORATOR:
                    spawnFilter[3] += 1;
                    break;
                case NET_GUN:
                    spawnFilter[4] += 1;

            }
        }
        for(int i = 0; i < spawnFilter.length; i++) {
            if (spawnFilter[i] == 0) {
                for (Direction dir : Direction.allDirections()) {
                    if (PlayerBot.tryBuild(spawnList[i], dir)) {
                        MapLocation loc = rc.getLocation().add(dir);
                        Blockchain.sendStatusUpdate(MSG_STATUS_UPDATE,
                                new int[]{UPD_BLD_BUILT, BLD_DESIGNSCH,
                                        loc.x, loc.y},
                                10);
                        refineries.add(rc.getLocation());
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
