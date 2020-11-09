package team8player.Robots;

import battlecode.common.*;
import team8player.Blockchain;
import static team8player.Globals.*;

import java.util.LinkedList;



public abstract class Unit implements PlayerBot {

    public Unit() {
    }

    static int findHQ() throws GameActionException {
        for (RobotInfo robot : rc.senseNearbyRobots()) {
            if(robot.type == RobotType.HQ) {
                if(robot.team == rc.getTeam()) {
                    Blockchain.sendRobotLoc(rc.getLocation(), BLD_HQ, HOS_ALLY, 10);
                    return HOS_ALLY;
                }
                else {
                    Blockchain.sendRobotLoc(rc.getLocation(), BLD_HQ, HOS_ENEMY, 10);
                    return HOS_ENEMY;
                }
            }
        }
        return -1;
    }

    static boolean tryMove() throws GameActionException {
        for (Direction dir : Direction.allDirections())
            if (tryMove(dir))
                return true;
        return false;
    }

    /**
     * Attempts to move in a given direction.
     *
     * @param dir The intended direction of movement
     * @return true if a move was performed
     * @throws GameActionException
     */
    static boolean tryMove(Direction dir) throws GameActionException {

        if (rc.isReady() && rc.canMove(dir)) {
            rc.move(dir);
            return true;
        } else return false;
    }

    public void startOfTurn() throws GameActionException {
        if(HqLocation == null || enemyHqLocation == null) {
            //check the blockchain every n turns for either HQ location
            if(rc.getRoundNum() % 3 == 0) {
                if(HqLocation == null) {
                    int[] filter = {teamCode, MSG_ROBOT_LOCATON,
                            BLD_HQ, HOS_ALLY, -1, -1, -1};
                    LinkedList<int[]> tmpa = Blockchain.getMessages(1, filter);
                    int[] message = {0};
                    if(!tmpa.isEmpty()) { message = tmpa.get(0); }
                    if(message[0] == teamCode) { HqLocation = new MapLocation(message[4], message[5]); }
                    System.out.printf("I just updated the location an ally HQ from the Blockchain!%n");
                }
                if(enemyHqLocation == null) {
                    int[] filter = {teamCode, MSG_ROBOT_LOCATON,
                            BLD_HQ, HOS_ALLY, -1, -1, -1};
                    LinkedList<int[]> tmpe = Blockchain.getMessages(1, filter);
                    int[] message = {0};
                    if(!tmpe.isEmpty()) { message = tmpe.get(0); }
                    if(message[0] == teamCode) { enemyHqLocation = new MapLocation(message[4], message[5]); }
                    System.out.printf("I just updated the location an enemy HQ from the Blockchain!%n");
                }
            }
            findHQ();
        }
    }
    public abstract void run() throws GameActionException;
}
