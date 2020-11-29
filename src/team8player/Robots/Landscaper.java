package team8player.Robots;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import team8player.Globals;

import static team8player.Globals.*;

import java.util.LinkedList;

public class Landscaper extends Unit {

    /**
     * Robot constructor
     * @return a random RobotType
     */
    public Landscaper(RobotController rc) {
        super(rc);
    }

    @Override
    public void run() throws GameActionException {
        super.run();

        switch(stratPhase) {
            case STR_PHS_EXPAND:
                break;
            case STR_PHS_SEARCH:
                break;
            case STR_PHS_DESTROY:
                break;
            default:
                break;
        }

        int currDirt = rc.getDirtCarrying();
        if (currDirt == 0) {
            Direction dir = PlayerBot.randomDirection();
            if (tryDig(dir)) {
                //System.out.println("I dug in the " + dir + " direction.");
                return;
            }
        }
        else if (currDirt == 25) {
            Direction dir = PlayerBot.randomDirection();
            if (tryDepositDirt(dir)) {
                //System.out.println("I deposited dirt in the " + dir + " direction.");
                return;
            }
        }

        else {
            for (Direction dir : Direction.allDirections())
                if (tryDig(dir)) {
                    //System.out.println("I dug in the " + dir + " direction.");
                    return;
                }
            for (Direction dir : Direction.allDirections())
                if (tryDepositDirt(dir)){
                    //System.out.println("I deposited dirt in the " + dir + " direction.");
                    return;
                }
        }
        endTurn();
    }

    /**
     * Attempts to dig in a given direction
     *
     * @param dir The intended direction to dig
     * @return true if it dug
     * @throws GameActionException
     */
    static boolean tryDig(Direction dir) throws GameActionException {
        if (rc.canDigDirt(dir)) {
            rc.digDirt(dir);
            return true;
        }
        return false;
    }

    /**
     * Attempts to deposit dirt in a given direction
     *
     * @param dir The intended direction to deposit
     * @return true if deposited
     * @throws GameActionException
     */
    static boolean tryDepositDirt(Direction dir) throws GameActionException {
        if(rc.canDepositDirt(dir)) {
            rc.depositDirt(dir);
            return true;
        }
        return false;
    }
}
