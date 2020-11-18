package team8player.Robots;

import battlecode.common.*;
import static team8player.Globals.*;
import jdk.nashorn.internal.objects.Global;
import team8player.Blockchain;
import team8player.RobotPlayer;
import java.util.Map;

public abstract class PlayerBot {

    public static RobotController rc;

    public PlayerBot(RobotController rc) {
        PlayerBot.rc = rc;
    }
    /**
     * Returns a random Direction.
     *
     * @return a random Direction
     */
    static Direction randomDirection() throws GameActionException {
        return Direction.values()[(int)(Math.random() * Direction.values().length)];
    }

    public static boolean tryBuild(RobotType type) throws GameActionException {

        for(Direction dir: Direction.allDirections()) {
            if(tryBuild(type, dir))
                return true;
        }
        return false;
    }

    public static void incrementBuiltType(RobotType type, MapLocation loc) {
        switch (type) {
            case MINER:
                numMiners++;
            case LANDSCAPER:
                numLandscapers++;
            case DELIVERY_DRONE:
                numDrones++;
            case HQ:
                return;
            case REFINERY:
                refineries.add(loc);
            case VAPORATOR:
                vaporators.add(loc);
            case DESIGN_SCHOOL:
                designSchools.add(loc);
            case FULFILLMENT_CENTER:
                fulCenters.add(loc);
            case NET_GUN:
                netGuns.add(loc);
        }
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
        MapLocation loc = rc.getLocation().add(dir);
        if(!isUnit(type))
            // if trying to build a building in a spot adjacent to another building, return false
            for(Direction tmpDir: Direction.cardinalDirections()) {
                MapLocation check = loc.add(tmpDir);
                for(RobotInfo rbt: nearbyBots) {
                    if(rbt.location.equals(check) && !isUnit(rbt.type)) {
                        System.out.println("Skipped building adjacent to another building.");
                        return false;
                    }
                }
            }

        // try to build
        if (rc.isReady() && rc.canBuildRobot(type, dir)) {
            rc.buildRobot(type, dir);
            Blockchain.sendMessage(MSG_RBT_BUILT, new int[]{robotToInt(type), loc.x, loc.y}, 10);
            incrementBuiltType(type, loc);
            return true;
        }
        else return false;
    }

    // Creating abstract methods for polymorphism
    public void run() throws GameActionException {};
}
