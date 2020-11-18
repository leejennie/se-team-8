package team8player;
import battlecode.common.*;
import team8player.Robots.*;
import static team8player.Globals.*;

import java.util.*;

import static battlecode.common.GameConstants.BLOCKCHAIN_TRANSACTION_LENGTH;

public strictfp class RobotPlayer {
    static PlayerBot rbt;
    static RobotType rType;

    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * If this method returns, the robot dies!
     **/
    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {

        // This is the RobotController object. You use it to perform actions from this robot,
        // and to get information on its current status.
        Globals.rc = rc;
        rType = rc.getType(); // Get the robot type and store it to reduce bytecode consumption with later calls
        // Create a robot with the Robot Controller
        switch (rType) {
            case MINER:
                rbt = new Miner(rc);
                break;
            case LANDSCAPER:
                rbt = new Landscaper(rc);
                break;
            case DELIVERY_DRONE:
                rbt = new DeliveryDrone(rc);
                break;
            case HQ:
                MapLocation loc = rc.getLocation();
                Blockchain.sendStatusUpdate(UPD_RBT_BUILT, new int[]{BLD_HQ, loc.x, loc.y}, 10);
                rbt = new HQ(rc);
                break;
            case REFINERY:
                rbt = new Refinery(rc);
                break;
            case VAPORATOR:
                rbt = new Vaporator(rc);
                break;
            case DESIGN_SCHOOL:
                rbt = new DesignSchool(rc);
                break;
            case FULFILLMENT_CENTER:
                rbt = new FulfillmentCenter(rc);
                break;
            case NET_GUN:
                rbt = new NetGun(rc);
                break;
        }

        turnCount = 0;

        //System.out.println("I'm a " + rType + " and I just got created!");
        while (true) {
            turnCount += 1;
            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode
            try {
                // Here, we've separated the controls into a different method for each RobotType.
                // You can add the missing ones or rewrite this into your own control structure.

                System.out.println("I'm a " + rType + "! Location " + rc.getLocation());

                //System.out.println("I'm a " + rType + "! Location " + rc.getLocation());

                rbt.run();

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println(rType + " Exception");
                e.printStackTrace();
            }
        }
    }
}
