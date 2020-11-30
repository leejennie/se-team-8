package team8player.Robots;

import battlecode.common.*;
import team8player.Blockchain;

import static team8player.Globals.*;

public class HQ extends Building {

    /**
     * Robot constructor
     * @return a HQ building
     */
    public HQ(RobotController rc) throws GameActionException {
        super(rc);
    }

    @Override
    public void run() throws GameActionException {

        // HQ is a singleton, so have it control strategy phase
        switch(stratPhase) {
            // If grown enough, switch to search phase
            case STR_PHS_EXPAND:
                if(refineries.size() == 2 && designSchools.size() == 1 && fulCenters.size() == 1) {
                    stratPhase = STR_PHS_SEARCH;
                    Blockchain.sendMessage(MSG_PHS_CHANGE, new int[] {STR_PHS_SEARCH}, 20);
                }
                // create 5 miners for the hq, and 3 for each refinery after
                if(miners.size() < ((refineries.size() - 1) * 3) + 5) {
                    tryBuild(RobotType.MINER);
                }
                break;
            case STR_PHS_SEARCH:
                // If we know enemy HQ location and have the attack force needed, switch to destroy phase
                if(enemyHqLocation != null && drones.size() > 2 && landscapers.size() > 2) {
                    stratPhase = STR_PHS_DESTROY;
                    Blockchain.sendMessage(MSG_PHS_CHANGE, new int[] {STR_PHS_DESTROY}, 20);
                }
                break;
            case STR_PHS_DESTROY:
                // If we lost our attack force, reset to expand phase
                if(drones.size() < 2 || landscapers.size() < 2) {
                    stratPhase = STR_PHS_EXPAND;
                    Blockchain.sendMessage(MSG_PHS_CHANGE, new int[] {STR_PHS_EXPAND}, 20);
                }
                break;
        }

        super.run();
        // make sure refineries isn't empty to avoid dividing by 0
        if(refineries.size() == 0) {
            refineries.add(rc.getLocation());
        }

        // check if there's anything it can use the net gun on
        nearbyBots = rc.senseNearbyRobots();
        for(RobotInfo rbt: nearbyBots) {
            if(rbt.type == RobotType.DELIVERY_DRONE && rbt.team != rc.getTeam())
                if(rc.canShootUnit(rbt.ID))
                    rc.shootUnit(rbt.ID);
        }

    }
}
