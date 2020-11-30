package team8player;

import battlecode.common.*;
import com.sun.tools.javac.code.Type;

import java.util.LinkedList;

public class Globals {

    // Global Variables
    public static int stratPhase = 0;
    public static int selfPhase = 0;
    public static MapLocation HqLocation;
    public static MapLocation enemyHqLocation;
    public static MapLocation currentGoal;
    public static MapLocation currentLoc;
    public static LinkedList<MapLocation> soupLocs = new LinkedList<>();
    public static LinkedList<MapLocation> usedLocs = new LinkedList<>();
    public static LinkedList<MapLocation> refineries = new LinkedList<>();
    public static LinkedList<MapLocation> vaporators = new LinkedList<>();
    public static LinkedList<MapLocation> designSchools = new LinkedList<>();
    public static LinkedList<MapLocation> fulCenters = new LinkedList<>();
    public static LinkedList<MapLocation> netGuns = new LinkedList<>();
    public static Direction currDirection = Direction.values()[(int) (Math.random() * Direction.values().length)];
    public static int dirTurnsLeft = 0;
    public static RobotInfo[] nearbyBots;
    public static int turnCount;
    public static LinkedList<Integer[]> miners;
    public static LinkedList<Integer[]> landscapers;
    public static LinkedList<Integer[]> drones;
    public static int lastCheckin = -1; // to track last time checked in
    public static RobotController rc;
    public static int txLength = 7; //added because trying to use the provided one was causing issues
    public static int lastTurnUpdatedFromBC = 1;
    public static boolean skipMovement = false;

    public static final int teamCode = 2662718; // Randomly generated number for id
    public static final int selfID = (int)(Math.random() * 9999999);

    // todo create alternate sabotage method that just takes the average of each
    // index of all messages that aren't ours and uses that as the trash message to send out

    /*
    // Declaring global constants and int IDs for transactions to make code more readable
    */

    // Strategy phase IDs
    public static final int STR_PHS_EXPAND      = 0;
    public static final int STR_PHS_SEARCH      = 1;
    public static final int STR_PHS_DESTROY     = 2;

    // Message type IDs
    public static final int MSG_ROBOT_LOCATON   = 0;
    public static final int MSG_SOUP_LOCATION   = 1;
    public static final int MSG_COW_LOCATION    = 2;
    public static final int MSG_RBT_BUILT       = 3;
    public static final int MSG_CHECK_IN        = 4;
    public static final int MSG_PHS_CHANGE      = 5;

    // Hostility
    public static final int HOS_ALLY            = 0;
    public static final int HOS_ENEMY           = 1;

    // Robot IDs
    public static final int UNT_MINER           = 0;
    public static final int UNT_LANDSCAPER      = 1;
    public static final int UNT_DDRONE          = 2;
    // buildings
    public static final int BLD_HQ              = 3;
    public static final int BLD_REFINERY        = 4;
    public static final int BLD_VAPORATOR       = 5;
    public static final int BLD_DESIGNSCH       = 6;
    public static final int BLD_FLMTCNTR        = 7;
    public static final int BLD_NETGUN          = 8;
    // cows
    public static final int UNT_COW             = 9;

    // Other various globals
    public static final int MAX_TURNS_DIR       = 30;
    public static final int TURNS_TIL_DEAD      = 6;
    public static final int MINER_RADIUS        = 5;



    // Helper functions
    public static int robotToInt(RobotType rType) {
        switch (rType) {
            case MINER:
                return UNT_MINER;
            case LANDSCAPER:
                return UNT_LANDSCAPER;
            case DELIVERY_DRONE:
                return UNT_DDRONE;
            case HQ:
                return BLD_HQ;
            case REFINERY:
                return BLD_REFINERY;
            case VAPORATOR:
                return BLD_VAPORATOR;
            case DESIGN_SCHOOL:
                return BLD_DESIGNSCH;
            case FULFILLMENT_CENTER:
                return BLD_FLMTCNTR;
            case NET_GUN:
                return BLD_NETGUN;
            case COW:
                return UNT_COW;
        }
        return -1;
    }

    public static String intToRobot(int rType) {
        switch (rType) {
            case UNT_MINER:
                return "MINER";
            case UNT_LANDSCAPER:
                return "LANDSCAPER";
            case UNT_DDRONE:
                return "DELIVERYDRONE";
            case BLD_HQ:
                return "HQ";
            case BLD_REFINERY:
                return "REFINERY";
            case BLD_VAPORATOR:
                return "VAPORATOR";
            case BLD_DESIGNSCH:
                return "DESIGNSCHOOL";
            case BLD_FLMTCNTR:
                return "FULFILLMENTCENTER";
            case BLD_NETGUN:
                return "NETGUN";
            case UNT_COW:
                return "COW";
        }
        return "idk!?!?";
    }

    public static String getHostility(int id) {
        if(id == HOS_ALLY) { return "ally";}
        return "enemy";
    }

    public static String getMessageType(int id) {
        switch (id) {
            case MSG_ROBOT_LOCATON:
                return "Robot Location";
            case MSG_SOUP_LOCATION:
                return "Soup Location";
            case MSG_COW_LOCATION:
                return "Cow Location";
            case MSG_RBT_BUILT:
                return "Robot Built";
        }
        return "!?!?";
    }

    public static boolean isUnit(RobotType type) {
        if(type == RobotType.MINER || type == RobotType.DELIVERY_DRONE ||
                type == RobotType.LANDSCAPER || type == RobotType.COW)
            return true;
        return false;
    }
}
