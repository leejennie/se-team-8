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
                Blockchain.sendMessage(MSG_ROBOT_LOCATON, new int[]{BLD_HQ, robot.location.x, robot.location.y}, 10);
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
        //System.out.printf("Trying to move to [%d] [%d]%n", tmp.x, tmp.y);
        if(rc.canSenseLocation(rc.getLocation().add(dir))) {
            if (rc.isReady() && rc.canMove(dir)) {
                rc.move(dir);
                return true;
            }
        }
        return false;
    }

    public void run() throws GameActionException {
        currentLoc = rc.getLocation();
        Blockchain.updateListsFromBC();
        if(lastCheckin < rc.getRoundNum() - 3) // check in every 3 turns
            Blockchain.sendMessage(MSG_CHECK_IN,new int[]{selfPhase,
                                currentLoc.x, currentLoc.y, robotToInt(rc.getType())}, 5);
        nearbyBots = rc.senseNearbyRobots();

        // the rest of this method is now covered by updateListsFromBC
        if(enemyHqLocation == null) {
            findHQ();
        }
    }

    static void endTurn() throws GameActionException {

        // If currentGoal != null, move in that direction
        if(currentGoal != null) tryMove(rc.getLocation().directionTo(currentGoal));

        // If nothing else to do, move in a random dir
        if(!skipMovement)
            tryMove();
    }
}
