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
        rType = rc.getType(); // Get the robot type and store it to reduce bytecode consumprion with later calls
        // Create a robot with the Robot Controller
        switch (rType) {
            case MINER:
                rbt = new Miner();
                break;
            case LANDSCAPER:
                rbt = new Landscaper();
                break;
            case DELIVERY_DRONE:
                rbt = new DeliveryDrone();
                break;
            case HQ:
                rbt = new HQ();
                break;
            case REFINERY:
                rbt = new Refinery();
                break;
            case VAPORATOR:
                rbt = new Vaporator();
                break;
            case DESIGN_SCHOOL:
                rbt = new DesignSchool();
                break;
            case FULFILLMENT_CENTER:
                rbt = new FulfillmentCenter();
                break;
            case NET_GUN:
                rbt = new NetGun();
                break;
        }

        turnCount = 0;

        System.out.println("I'm a " + rType + " and I just got created!");
        while (true) {
            turnCount += 1;
            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode
            try {
                // Here, we've separated the controls into a different method for each RobotType.
                // You can add the missing ones or rewrite this into your own control structure.
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
