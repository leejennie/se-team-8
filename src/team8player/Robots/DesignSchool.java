package team8player.Robots;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import team8player.Globals;

import static team8player.Globals.*;

public class DesignSchool extends Building {

    /**
     * Robot constructor
     * @return a random RobotType
     */
    public DesignSchool(RobotController rc) throws GameActionException {
        super(rc);
    }

    @Override
    public void run() throws GameActionException {
        super.run();

        if(numLandscapers < 3) { // just an arbitrary limit until we decide on a strategy for these
            for (Direction dir : Direction.allDirections())
                if (PlayerBot.tryBuild(RobotType.LANDSCAPER, dir)) {
                    System.out.println("Landscaper created");
                }
        }
    }
}
