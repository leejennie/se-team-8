package team8player.Robots;

import battlecode.common.*;

import static team8player.Globals.*;

public class Vaporator extends Building {

    /**
     * Robot constructor
     * @return a random RobotType
     */
    public Vaporator() {
    }

    @Override
    public void run() throws GameActionException {
        super.run();
        for (Direction dir : Direction.allDirections()) {
            int roundNum = rc.getRoundNum();
            System.out.println("round number is "+ roundNum);
            if(roundNum>300){
                PlayerBot.tryBuild(RobotType.VAPORATOR,dir);
                System.out.println("create a vaporator");
            }
            int soupProduced = RobotType.VAPORATOR.maxSoupProduced;


        }
    }

    }
