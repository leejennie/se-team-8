package team8player;

import battlecode.common.*;
import static team8player.Globals.*;
import java.util.*;

public class Blockchain {

    // Takes a filter for messages and returns the first matching message with -1 representing a wildcard
    // Ex to get enemy HQ Location:
    // getMessageFromBlockchain([teamCode, message_type.HQ_LOCATION.id, _robot_type._HQ.id, 1, -1, -1, -1]);
    public static LinkedList<int[]> getMessages(int n, int[] msgFilter) throws GameActionException {
        int count = 0;
        LinkedList<int[]> result = new LinkedList<int[]>() {};
        for(int i = lastTurnUpdatedFromBC; i < rc.getRoundNum(); i++) {
            for(Transaction t : rc.getBlock(i)) {
                int[] message = t.getMessage();
                for(int j = 0; j < txLength; j++) {
                    if(msgFilter[j] == -1 || msgFilter[j] == message[j]) {
                        if(j == txLength - 1) {
                            //System.out.printf("I received a %s message from the Blockchain!%n",
                                    //getMTDescFromId(msgFilter[1]));
                            result.add(message);
                            count++;
                            if(count == n) { return result;}
                        }
                    }
                    else {
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static LinkedList<int[]> getTeamMessages() throws GameActionException {
        int[] filter = {teamCode, -1, -1, -1, -1, -1, -1};
        return getMessages(-1, filter);
    }

    public static boolean checkListForLoc(LinkedList<MapLocation> locs, MapLocation loc) {
        for(MapLocation l: locs) {
            if(l.equals(loc))
                return true;
        }
        return false;
    }

    public static boolean checkListForBot(LinkedList<Integer[]> bots, int botID) {
        for(Integer[] bot: bots) {
            if (botID == bot[0])
                return true;
        }
        return false;
    }

    // Made as a pseudo template for future types of messages.
    public static void sendMessage(int messageType, int[] values, int txCost) throws GameActionException {
        int[] message = new int[txLength];
        message[0] = teamCode;
        message[1] = selfID;
        message[2] = messageType;
        int i = 3;
        for(int x: values) {
            message[i] = x;
            i++;
            if(i == txLength)
                break;
        }
        if(rc.canSubmitTransaction(message, txCost)) {
            rc.submitTransaction(message, txCost);
            //System.out.printf("I sent a %s message to the blockchain for %d!%n", getUpdateType(message[2]), txCost);
        }
    }

    /*
     * Parse relevant messages from the blockchain
     */
    public static void updateListsFromBC() throws GameActionException {
        LinkedList<int[]> messages = getTeamMessages();
        for(int[] msg: messages) {
            //if(msg[1] == selfID) -- was using this to filter out own messages, but could be useful
                //continue;
            switch (msg[2]) {
                case MSG_ROBOT_LOCATON:
                    parseRobotLoc(msg);
                    break;
                case MSG_SOUP_LOCATION:
                    parseSoupLoc(msg);
                    break;
                case MSG_COW_LOCATION:
                    //parseCowLocation(msg); -- todo
                    break;
                case MSG_RBT_BUILT:
                    updateRobotBuilt(msg);
                case MSG_CHECK_IN:
                    checkIn(msg);
                    break;
                case MSG_PHS_CHANGE:
                    stratPhase = msg[3];
                default:
                    break;
            }
        }
        lastTurnUpdatedFromBC = turnCount;
    }

    public static void parseRobotLoc(int[] message) {
        switch(message[3]) {
            case UNT_MINER:
                break;
            case UNT_LANDSCAPER:
                break;
            case UNT_DDRONE:
                break;
            case BLD_HQ:
                break;
            case BLD_REFINERY:
                break;
            case BLD_VAPORATOR:
                break;
            case BLD_DESIGNSCH:
                break;
            case BLD_FLMTCNTR:
                break;
            case BLD_NETGUN:
                break;
            case UNT_COW:
                break;
            default:
                break;
        }
    }

    public static void parseSoupLoc(int[] message) {
        soupLocs.add(new MapLocation(message[2], message[3]));
    }

    /*public static void parseUpdate(int[] message) {
        switch(message[2]) {
            case UPD_ROBOT_LOCATION:
                updateRobotLoc(message);
            case UPD_RBT_BUILT:
                updateRobotBuilt(message);
            case UPD_SOUP_USED:
                moveSoupToUsed(message);
            default:
                break;
        }
    }*/

    public static void updateRobotLoc(int[] message) {
        switch (message[3]) {
            case UNT_MINER:
                break;
            case UNT_LANDSCAPER:
                break;
            case UNT_DDRONE:
                break;
            case BLD_HQ:
                enemyHqLocation = new MapLocation(message[4], message[5]);
                break;
            case BLD_REFINERY:
                break;
            case BLD_VAPORATOR:
                break;
            case BLD_DESIGNSCH:
                break;
            case BLD_FLMTCNTR:
                break;
            case BLD_NETGUN:
                break;
            case UNT_COW:
                break;
            default:
                break;
        }
    }

    public static void updateRobotBuilt(int[] message) {
        MapLocation loc = new MapLocation(message[4], message[5]);
        int round = rc.getRoundNum();
        switch (message[3]) {
            case UNT_MINER:
                if(!checkListForBot(miners, message[1]))
                    miners.add(new Integer[]{message[1], message[3], message[4], message[5], round});
                break;
            case UNT_LANDSCAPER:
                if(!checkListForBot(landscapers, message[1]))
                    landscapers.add(new Integer[]{message[1], message[3], message[4], message[5], round});
                break;
            case UNT_DDRONE:
                if(!checkListForBot(drones, message[1]))
                    drones.add(new Integer[]{message[1], message[3], message[4], message[5], round});
                break;
            case BLD_HQ:
                HqLocation = loc;
                if(!checkListForLoc(refineries, loc))
                    refineries.add(loc);
                break;
            case BLD_REFINERY:
                if(!checkListForLoc(refineries, loc))
                    refineries.add(loc);
                break;
            case BLD_VAPORATOR:
                if(!checkListForLoc(vaporators, loc))
                    vaporators.add(loc);
                break;
            case BLD_DESIGNSCH:
                if(!checkListForLoc(designSchools, loc))
                    designSchools.add(loc);
                break;
            case BLD_FLMTCNTR:
                if(!checkListForLoc(fulCenters, loc))
                    fulCenters.add(loc);
                break;
            case BLD_NETGUN:
                if(!checkListForLoc(netGuns, loc))
                    netGuns.add(loc);
                break;
            case UNT_COW:
                break;
            default:
                break;
        }
    }

    // Updates the last turn heard from bot and removes bots assumed to be destroyed
    public static void checkIn(int[] message) {
        switch(message[6]) {
            case UNT_MINER:
                checkIn(miners, message);
                break;
            case UNT_LANDSCAPER:
                checkIn(landscapers, message);
                break;
            case UNT_DDRONE:
                checkIn(drones, message);
                break;
            default:
                break;
        }
    }

    public static void checkIn(LinkedList<Integer[]> lst, int[] message) {
        int round = rc.getRoundNum();
        boolean found = false;
        for(Integer[] unt: lst) {
            if(unt[0] == message[1]) {
                found = true;
                unt[unt.length - 1] = round;
            }
            else if(unt[unt.length - 1] < round - TURNS_TIL_DEAD)
                lst.remove(unt);
        }
        if(!found) {
            lst.add(new Integer[]{message[1], message[3], message[4], message[5], round});
        }
    }

    public static void moveSoupToUsed(int[] message) {
        MapLocation tmp = new MapLocation(message[3], message[4]);
        if(soupLocs.contains(tmp))
            soupLocs.remove(tmp);
        if(!usedLocs.contains(tmp))
            usedLocs.add(tmp);
    }


    // Sabotage methods
    public static int getEnemyBCIdentifier() throws GameActionException {
        // Build a hashmap of all the ints from the blockchain
        HashMap<Integer, Integer> collisionCounter = new HashMap<Integer, Integer>();
        for(int i = 0; i < rc.getRoundNum(); i++) {
            for(Transaction tx : rc.getBlock(i)) {
                int[] message = tx.getMessage();
                for(int j: message) {
                    Integer count = collisionCounter.get(j);
                    collisionCounter.put(j, count != null ? count+1 : 1);
                }
            }
        }
        // Get max value to find key, which is likely the enemy's message identifier
        return Collections.max(collisionCounter.entrySet(),
                new Comparator<Map.Entry<Integer, Integer>>() {
                    public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                        return o1.getValue().compareTo(o2.getValue());
                    }
                }
        ).getKey();
    }

    public static void sendJunkMessages(int enemyId) throws GameActionException{
        int[] message = new int[txLength];
        message[0] = enemyId;
        for(int i = 1; i < txLength; i++) {
            Random rand = new Random();
            message[i] = rand.nextInt(9999999);
        }
        if(rc.canSubmitTransaction(message, 10))
            rc.submitTransaction(message, 10);
    }
}