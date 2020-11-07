package team8player;

import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import com.sun.tools.javac.code.Type;

import java.util.LinkedList;

public class Globals {

    // Global Variables
    public static MapLocation HqLocation;
    public static MapLocation enemyHqLocation;
    public static LinkedList<MapLocation> goalLocs;
    public static LinkedList<MapLocation> usedLocs;
    public static int turnCount;
    public static int numMiners = 0;
    public static int lastAction = -1; // so we can know when a bot is doing something different on their current turn
    public static int countDesignSchool = 0;
    public static int countRefinery = 0;
    public static RobotController rc;


    public static final int teamCode = 2662718; // Randomly generated number for id

    // todo create alternate sabotage method that just takes the average of each
    // index of all messages that aren't ours and uses that as the trash message to send out

    /*
    // Declaring global constants and int IDs for transactions to make code more readable
    */
    // Message type IDs
    public static final int MSG_ROBOT_LOCATON   = 0;
    public static final int MSG_SOUP_LOCATION   = 1;
    public static final int MSG_STATUS_UPDATE   = 2;

    // Status Update type IDs
    public static final int UPD_ROBOT_LOCATION  = 0;
    public static final int UPD_SOUP_LOCATION   = 1;
    public static final int UPD_COW_LOCATION    = 2;

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

}
