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

    public void run() throws GameActionException {
        super.run();
        //System.out.println("Pollution: " + rc.sensePollution(rc.getLocation()));
    }
}
