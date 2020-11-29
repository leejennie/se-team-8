package team8player.Robots;

import battlecode.common.*;
import team8player.Blockchain;

import team8player.Globals;

import static team8player.Globals.*;

public class FulfillmentCenter extends Building {

    /**
     * Robot constructor
     * @return a random RobotType
     */
    public FulfillmentCenter(RobotController rc) {
        super(rc);
    }

    @Override
    public void run() throws GameActionException {
        super.run();

        switch (stratPhase) {
            case STR_PHS_EXPAND:
                if(drones.size() < 3)
                    tryBuild(RobotType.DELIVERY_DRONE);
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
