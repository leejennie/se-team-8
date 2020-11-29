package team8player.Robots;

import battlecode.common.*;

import static team8player.Globals.*;
import static team8player.Globals.STR_PHS_DESTROY;

public class DeliveryDrone extends Unit {

    /**
     * Robot constructor
     * @return a random RobotType
     */
    public DeliveryDrone(RobotController rc) {
        super(rc);
    }

    @Override
    public void run() throws GameActionException {
        super.run();

        switch(stratPhase) {
            case STR_PHS_EXPAND:
                break;
            case STR_PHS_SEARCH:
                break;
            case STR_PHS_DESTROY:
                break;
            default:
                break;
        }

        Team enemy = rc.getTeam().opponent();
        if (!rc.isCurrentlyHoldingUnit()) {
            // See if there are any enemy robots within capturing range
            RobotInfo[] robots = rc.senseNearbyRobots(GameConstants.DELIVERY_DRONE_PICKUP_RADIUS_SQUARED, enemy);

            if (robots.length > 0) {
                // Pick up a first robot within range
                rc.pickUpUnit(robots[0].getID());
                System.out.println("I picked up " + robots[0].getID() + "!");
            }
        } else {
            // No close robots, so search for robots within sight radius
            Unit.tryMove(PlayerBot.randomDirection());
        }
    }
}
