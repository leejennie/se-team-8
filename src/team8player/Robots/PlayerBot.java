package team8player.Robots;

import battlecode.common.*;
import jdk.nashorn.internal.objects.Global;
import team8player.Blockchain;
import team8player.Globals;
import team8player.RobotPlayer;
import java.util.Map;

public class PlayerBot {

    public static RobotController rc;

    public PlayerBot(RobotController rc) {
        this.rc = rc;
    }
    /**
     * Returns a random Direction.
     *
     * @return a random Direction
     */
    static Direction randomDirection() throws GameActionException {
        return Direction.values()[(int)(Math.random() * Direction.values().length)];
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
        }
        else return false;
    }

    // Creating abstract methods for polymorphism
    public void run() throws GameActionException {};
}
