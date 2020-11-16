package team8player.Robots;

import battlecode.common.*;
import team8player.Blockchain;
import static team8player.Globals.*;

import java.util.LinkedList;



public abstract class Unit implements PlayerBot {

    public Unit() {
    }

    static void findHQ() throws GameActionException {
        for (RobotInfo robot : nearbyBots) {
            if(robot.type == RobotType.HQ) {
                Blockchain.sendRobotLoc(rc.getLocation(), BLD_HQ, 10);
            }
        }
    }

    static boolean tryMove() throws GameActionException {
        while(true) {
            if(tryMove(Direction.values()[(int) Math.random() * Direction.values().length]))
                return true;
        }
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

    public void startOfTurn() throws GameActionException {}

    public void run() throws GameActionException {
        Blockchain.updateListsFromBC();
        nearbyBots = rc.senseNearbyRobots();

        if(HqLocation == null || enemyHqLocation == null) {
            //check the blockchain every n turns for either HQ location
            if(rc.getRoundNum() % 3 == 0) {
                if(HqLocation == null) {
                    int[] filter = {teamCode, MSG_ROBOT_LOCATON,
                            BLD_HQ, HOS_ALLY, -1, -1, -1};
                    LinkedList<int[]> tmp = Blockchain.getMessages(1, filter);
                    int[] message = {0};
                    if(!tmp.isEmpty()) { message = tmp.get(0); }
                    if(message[0] == teamCode) { HqLocation = new MapLocation(message[4], message[5]); }
                    System.out.printf("I just updated the location an ally HQ from the Blockchain!%n");
                }
                if(enemyHqLocation == null) {
                    int[] filter = {teamCode, MSG_ROBOT_LOCATON,
                            BLD_HQ, HOS_ALLY, -1, -1, -1};
                    LinkedList<int[]> tmp = Blockchain.getMessages(1, filter);
                    int[] message = {0};
                    if(!tmp.isEmpty()) { message = tmp.get(0); }
                    if(message[0] == teamCode) { enemyHqLocation = new MapLocation(message[4], message[5]); }
                    System.out.printf("I just updated the location an enemy HQ from the Blockchain!%n");
                }
            }
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
