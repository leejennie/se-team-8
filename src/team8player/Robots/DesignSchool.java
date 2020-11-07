package team8player.Robots;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import static team8player.Globals.*;

public class DesignSchool extends Building {

    /**
     * Robot constructor
     * @param rc the controller associated with this robot
     * @return a random RobotType
     */
    public DesignSchool() throws GameActionException {
    }

    @Override
    public void run() throws GameActionException {
        super.run();
        for (Direction dir : Direction.allDirections())
            if(PlayerBot.tryBuild(RobotType.LANDSCAPER, dir)) {
                System.out.println("Landscaper created");
            }
    }
}
