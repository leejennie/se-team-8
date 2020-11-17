package team8player.Robots;

import battlecode.common.*;
import team8player.Blockchain;
import static team8player.Globals.*;
import team8player.*;

import java.util.LinkedList;



public class Unit extends PlayerBot {

    public Unit(RobotController rc) {
        super(rc);
    }

    static void findHQ() throws GameActionException {
        for (RobotInfo robot : nearbyBots) {
            if(robot.type == RobotType.HQ && robot.team != rc.getTeam()) {
                Blockchain.sendRobotLoc(rc.getLocation(), BLD_HQ, 10);
                Blockchain.sendRobotLoc(rc.getLocation(), BLD_HQ, 10);
            }
        }
    }

    static boolean tryMove() throws GameActionException {
        if(dirTurnsLeft == 0) {
            Direction tmpDir = currDirection;
            while(tmpDir == currDirection) {
                tmpDir = Direction.values()[(int) (Math.random() * Direction.values().length)];
            }
            currDirection = tmpDir;
            dirTurnsLeft = (int)(Math.random() * MAX_TURNS_DIR) + 1;
        }
        if(!tryMove(currDirection)) {
            dirTurnsLeft = 0;
            return tryMove();
        }
        dirTurnsLeft--;
        return true;
    }

    /**
     * Attempts to move in a given direction.
     *
     * @param dir The intended direction of movement
     * @return true if a move was performed
     * @throws GameActionException
     */
    static boolean tryMove(Direction dir) throws GameActionException {
        MapLocation tmp = rc.getLocation().add(dir);
        System.out.printf("Trying to move to [%d] [%d]%n", tmp.x, tmp.y);
        if(rc.canSenseLocation(rc.getLocation().add(dir))) {
            if (rc.isReady() && rc.canMove(dir)) {
                rc.move(dir);
                return true;

            }
        }
        return false;
    }

    public void startOfTurn() throws GameActionException {}

    public void run() throws GameActionException {
        Blockchain.updateListsFromBC();
        nearbyBots = rc.senseNearbyRobots();

        // the rest of this method is now covered by updateListsFromBC
        if(enemyHqLocation == null) {
            findHQ();
        }
    }

    static void endTurn() throws GameActionException {

        // If currentGoal != null, move in that directio
        if(currentGoal != null) tryMove(rc.getLocation().directionTo(currentGoal));

        // If nothing else to do, move in a random dir
        tryMove();
    }
}
