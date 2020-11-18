package team8player.Robots;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import team8player.Globals;

import static team8player.Globals.*;

public class Refinery extends Building {

    /**
     * Robot constructor
     * @return a random RobotType
     */
    public Refinery(RobotController rc) {
        super(rc);
    }

    public void run() throws GameActionException {
        super.run();
        //System.out.println("Pollution: " + rc.sensePollution(rc.getLocation()));
    }
}
