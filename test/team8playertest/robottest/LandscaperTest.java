package team8playertest.robottest;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import org.junit.Test;
import org.mockito.Mockito;
import team8player.Robots.Landscaper;
import team8player.Robots.Miner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class LandscaperTest {

    @Test
    public void testLandscaperCreation() {
        Landscaper l1 = Mockito.mock(Landscaper.class);
        Landscaper l2 = Mockito.mock(Landscaper.class);
        assertNotEquals(l1, l2);
    }

    @Test
    public void testTryDig() throws GameActionException {
        Landscaper l1 = Mockito.mock(Landscaper.class);
        l1.rc = Mockito.mock(RobotController.class);
        if (l1.rc.getType() == RobotType.LANDSCAPER){
            when(l1.tryDig(Direction.NORTH)).thenReturn(true);
            assertEquals(l1.tryDig(Direction.NORTH), true);
        }
        l1.tryDig(Direction.NORTH);
    }

    @Test
    public void testTryDepositDirt() throws GameActionException {
        Landscaper l1 = Mockito.mock(Landscaper.class);
        l1.rc = Mockito.mock(RobotController.class);
        if (l1.rc.getType() == RobotType.LANDSCAPER){
            when(l1.tryDepositDirt(Direction.NORTH)).thenReturn(true);
            assertEquals(l1.tryDepositDirt(Direction.NORTH), true);
        }
        l1.tryDepositDirt(Direction.NORTH);
    }

    @Test // Need to fix this
    public void testRun() throws GameActionException {
        Landscaper l1 = Mockito.mock(Landscaper.class);
        l1.rc = Mockito.mock(RobotController.class);
        if (l1.rc.getType() == RobotType.LANDSCAPER) {
            doNothing().when(l1).run();
            verify(l1, times(1)).run();
        }
        l1.run();
    }
}
