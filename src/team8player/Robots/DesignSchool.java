package team8player.Robots;

import battlecode.common.*;
import team8player.Blockchain;

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

        if(numLandscapers <= numDrones && (enemyHqLocation != null || numLandscapers == 0)) { // just an arbitrary limit until we decide on a strategy for these
            tryBuild(RobotType.LANDSCAPER);
        }
    }
}
