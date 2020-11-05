package team8playertest;

import team8player.RobotPlayer;
import battlecode.common.*;
import battlecode.common.RobotController;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.*;


// First two functions written by Li (big thanks to her). Writing this comment to give her credit.
public class RobotPlayerTest {

    @Test
    public void testRobotPlayerCreation() {
        RobotPlayer r1 = Mockito.mock(RobotPlayer.class);
        RobotPlayer r2 = Mockito.mock(RobotPlayer.class);
        assertNotEquals(r1, r2);
    }

    @Test
    public void testGetType() {
        RobotController rc1 = Mockito.mock(RobotController.class);
        when(rc1.getType()).thenReturn(RobotType.MINER);
        assertEquals(rc1.getType(), RobotType.MINER);
    }

    //Make list of tests to do for each robot and move these to new files after refactoring

    @Test
    public void testCanBuildRobot() {
        RobotController rc1 = Mockito.mock(RobotController.class);
        if(rc1.getType() == RobotType.MINER) {
            when(rc1.canBuildRobot(RobotType.DESIGN_SCHOOL, Direction.CENTER)).thenReturn(true);
            assertEquals(rc1.canBuildRobot(RobotType.DESIGN_SCHOOL, Direction.CENTER), true);
        }
    }

    //Test miner can mine soup
    /*
    public void testTryMine() {
        RobotPlayer rp1 = Mockito.mock(RobotPlayer.class);
        if(rp1.getType() == RobotType.MINER) {
        rp1.rc = Mockito.mock(RobotController.class);
        doAnswer((i) -> {
            return true;
        }).when(rp1).tryMine();
        rp1.tryMine();
        verify(rp1, times(1)).tryMine();
        }
    }
    public void testTryMine() throws GameActionException {
        RobotController rc1 = Mockito.mock(RobotController.class);
        RobotPlayer rp1 = Mockito.mock(RobotPlayer.class);
        if (rc1.getType() == RobotType.MINER) {
            doAnswer((i) -> {
                return true;
            }).when(rp1).tryMine(Direction.CENTER);
            rp1.tryMine(Direction.CENTER);
            verify(rp1, times(1)).tryMine(Direction.CENTER);
        }
    }*/
}