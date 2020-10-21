package Tianhuifuncsplayer;
import battlecode.common.*;

public class Miner extends RobotPlayer{
    public Miner() throws GameActionException{
        //
    }
    public void run() throws GameActionException{


        tryBlockchain();
        tryMove(randomDirection());
        if (tryMove(randomDirection()))
            System.out.println("I moved!");
        // tryBuild(randomSpawnedByMiner(), randomDirection());

        //build fulfillment center
        for (Direction dir : directions)
            tryBuild(RobotType.FULFILLMENT_CENTER, dir);
        //build design school
        for (Direction dir : directions)
            tryBuild(RobotType.DESIGN_SCHOOL, dir);

        //transfer to refinery
        //rc.depositSoup();

        for (Direction dir : directions)
            if (tryRefine(dir))
                System.out.println("I refined soup! " + rc.getTeamSoup());
        for (Direction dir : directions)
            if (tryMine(dir))
                System.out.println("I mined soup! " + rc.getSoupCarrying());

    }

}
