package Tianhuifuncsplayer;
import battlecode.common.*;

public class HQ extends RobotPlayer {
    static RobotController rc;
    public HQ(RobotController rc) throws GameActionException {
        //
    }
    public void run() throws GameActionException{
        Direction dir = Direction.WEST;
        for(int i=0;i<6;i++){
            tryBuild(RobotType.MINER, dir);
            dir = dir.rotateRight();
            System.out.println("the direction is "+dir);
        }
    }

}
