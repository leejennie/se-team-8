package team8player.Robots;

import battlecode.common.*;
import team8player.Globals;

import static team8player.Globals.*;

public class HQ extends Building {

    /**
     * Robot constructor
     * @return a HQ building
     */
    public HQ(RobotController rc) {
        super(rc);
    }

    @Override
    public void run() throws GameActionException {
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

        //Taken from https://www.youtube.com/watch?v=B0dYT3KZd9Y lecture video. Liked the way they produced miners and
        // thought it was helpful to winning the game because having more miners can produce more "robots"
        if(numMiners/refineries.size() < 3) { // limit the number of miners to 3 for every refinery (HQ included)
            for (Direction dir : Direction.allDirections()) {
                if (PlayerBot.tryBuild(RobotType.MINER, dir)) {
                    numMiners++;
                    System.out.println("Miner created!");
                }
            }
        }
    }
}
