package team8player.Robots;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import static team8player.Globals.*;

public class Refinery extends Building {

    /**
     * Robot constructor
     * @return a random RobotType
     */
    public Refinery() {
    }

    /**
     * Attempts to refine soup in a given direction.
     * /
     *
     * @param dir The intended direction of refining
     * @return true if a move was performed
     * @throws GameActionException
     */
    static boolean tryRefine(Direction dir) throws GameActionException {
        if (rc.isReady() && rc.canDepositSoup(dir)) {
            rc.depositSoup(dir, rc.getSoupCarrying());
            return true;
        } else return false;
    }

    public void run() throws GameActionException {
        super.run();
        //System.out.println("Pollution: " + rc.sensePollution(rc.getLocation()));
    }
}
