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

        switch(stratPhase) {
            case STR_PHS_EXPAND:
                if(landscapers.size() < 3)
                    tryBuild(RobotType.LANDSCAPER);
                break;
            case STR_PHS_SEARCH:
                break;
            case STR_PHS_DESTROY:
                break;
            default:
                break;
        }
    }
}
