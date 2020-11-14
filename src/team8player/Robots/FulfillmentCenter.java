package team8player.Robots;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import static team8player.Globals.*;

public class FulfillmentCenter extends Building {

    /**
     * Robot constructor
     * @param rc the controller associated with this robot
     * @return a random RobotType
     */
    public FulfillmentCenter() {
    }

    @Override
    public void run() throws GameActionException {
        super.run();

        if(numDrones < refineries.size()) { // don't spawn more Drones than there are refineries
            for (Direction dir : Direction.allDirections()) {
                PlayerBot.tryBuild(RobotType.DELIVERY_DRONE, dir);
                numDrones++;
            }
        }
    }
}
