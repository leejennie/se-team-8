package team8player.Robots;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import static team8player.Globals.*;

public class NetGun extends Unit {

    /**
     * Robot constructor
     * @param rc the controller associated with this robot
     * @return a random RobotType
     */
    public NetGun() {
    }

    @Override
    public void run() throws GameActionException {
        super.run();
        for (int i = 0; i < 15; i++) {
            if (rc.canShootUnit(i))
                rc.shootUnit(i);
        }
    }
}
