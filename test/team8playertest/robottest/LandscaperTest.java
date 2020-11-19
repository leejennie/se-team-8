package team8playertest.robottest;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import org.junit.Test;
import org.mockito.Mockito;
import team8player.Robots.Landscaper;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


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
        when(l1.rc.getType()).thenReturn(RobotType.LANDSCAPER);
        when(l1.rc.canDigDirt(Direction.SOUTH)).thenReturn(true);
        l1.tryDig(Direction.SOUTH);
    }

    @Test
    public void testTryDigFalse() throws GameActionException {
        Landscaper l1 = Mockito.mock(Landscaper.class);
        l1.rc = Mockito.mock(RobotController.class);
        when(l1.rc.getType()).thenReturn(RobotType.LANDSCAPER);
        when(l1.rc.canDigDirt(Direction.SOUTH)).thenReturn(false);
        l1.tryDig(Direction.SOUTH);
    }

    @Test
    public void testTryDepositDirt() throws GameActionException {
        Landscaper l1 = Mockito.mock(Landscaper.class);
        l1.rc = Mockito.mock(RobotController.class);
        when(l1.rc.getType()).thenReturn(RobotType.LANDSCAPER);
        when(l1.rc.canDepositDirt(Direction.SOUTH)).thenReturn(true);
        l1.tryDepositDirt(Direction.SOUTH);
    }

    @Test
    public void testTryDepositDirtFalse() throws GameActionException {
        Landscaper l1 = Mockito.mock(Landscaper.class);
        l1.rc = Mockito.mock(RobotController.class);
        when(l1.rc.getType()).thenReturn(RobotType.LANDSCAPER);
        when(l1.rc.canDepositDirt(Direction.SOUTH)).thenReturn(false);
        l1.tryDepositDirt(Direction.SOUTH);
    }

}
