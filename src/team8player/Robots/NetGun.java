package team8player.Robots;

import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

import static team8player.Globals.*;

public class NetGun extends Unit {

    /**
     * Robot constructor
     * @return a random RobotType
     */
    public NetGun() {
    }

    @Override
    public void run() throws GameActionException {
        super.run();
        // check if there's anything it can use the net gun on
        nearbyBots = rc.senseNearbyRobots();
        for(RobotInfo rbt: nearbyBots) {
            if(rbt.type == RobotType.DELIVERY_DRONE && rbt.team != rc.getTeam())
                if(rc.canShootUnit(rbt.ID))
                    rc.shootUnit(rbt.ID);
        }
    }
}
