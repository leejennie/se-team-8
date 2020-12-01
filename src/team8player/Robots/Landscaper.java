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

        int currDirt = rc.getDirtCarrying();
        if (currDirt == 0) {
            Direction dir = PlayerBot.randomDirection();
            if (tryDig(dir)) {
                //System.out.println("I dug in the " + dir + " direction.");
                return;
            }
        }

        switch(stratPhase) {
            case STR_PHS_EXPAND:
            case STR_PHS_SEARCH:
                // build wall around HQ for either of first 2 phases
                currentGoal = refineries.getFirst();
                currDirection = currentLoc.directionTo(currentGoal);
                for(Direction dir: Direction.allDirections()) {
                    if(currentLoc.add(dir).distanceSquaredTo(currentGoal) < 3) {
                        if(rc.canDepositDirt(dir))
                            rc.depositDirt(dir);
                        else
                            tryMove();
                    }
                }
                if(rc.canDigDirt(currDirection.opposite()))
                    rc.digDirt(currDirection.opposite());
                break;
            case STR_PHS_DESTROY:
                currentGoal = enemyHqLocation;
                currDirection = currentLoc.directionTo(currentGoal);
                if(currentLoc.add(currDirection) == enemyHqLocation && Globals.rc.canDepositDirt(currDirection)
                    && currDirt > 0)
                    rc.depositDirt(currDirection);
                if(rc.canDigDirt(currDirection.opposite()))
                    rc.digDirt(currDirection.opposite());
                break;
            default:
                break;
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
