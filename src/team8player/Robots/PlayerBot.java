package team8player.Robots;

import battlecode.common.*;
import team8player.Blockchain;

import java.util.Map;

import static team8player.Globals.*;


public interface PlayerBot {

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
        } else return false;
    }

    // Creating abstract methods for polymorphism
    void run() throws GameActionException;
}
