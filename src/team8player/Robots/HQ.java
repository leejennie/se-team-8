package team8player.Robots;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import static team8player.Globals.*;

public class HQ extends Building {

    /**
     * Robot constructor
     * @param rc the controller associated with this robot
     * @return a HQ building
     */
    public HQ() {
    }

    @Override
    public void run() throws GameActionException {
        super.run();

        //Taken from https://www.youtube.com/watch?v=B0dYT3KZd9Y lecture video. Liked the way they produced miners and
        // thought it was helpful to winning the game because having more miners can produce more "robots"
        if(numMiners < 15 && turnCount<400) {
            for (Direction dir : Direction.allDirections()) {
                if (PlayerBot.tryBuild(RobotType.MINER, dir)) {
                    numMiners++;
                    System.out.println("Miner created!");
                }
            }
        }
    }
}
