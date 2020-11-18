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

        if(numDrones < refineries.size()) { // don't spawn more Drones than there are refineries
            tryBuild(RobotType.DELIVERY_DRONE);
        }
    }
}
