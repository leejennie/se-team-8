package team8playertest.robottest;

import team8player.Robots.Unit;
import battlecode.common.*;
import battlecode.common.RobotController;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UnitTest {

    @Test
    public void testUnitCreation() {
        Unit u1 = Mockito.mock(Unit.class);
        Unit u2 = Mockito.mock(Unit.class);
        assertNotEquals(u1, u2);
    }

    /*
    @Test // Need to fix!
    public void testFindHQ() throws GameActionException {
        Unit u1 = Mockito.mock(Unit.class);
        u1.rc = Mockito.mock(RobotController.class);
        when (u1.rc.getType()).thenReturn(RobotType.HQ);
        when (u1.rc.getTeam()).thenReturn(Team.A);
        u1.findHQ();
    }*/

    @Test
    public void testTryMove() throws GameActionException {
        Unit u1 = Mockito.mock(Unit.class);
        u1.rc = Mockito.mock(RobotController.class);
        MapLocation temp = new MapLocation(5,5);
        when (u1.rc.getType()).thenReturn(RobotType.MINER);
        when (u1.rc.getLocation()).thenReturn(temp);
        when (u1.rc.canSenseLocation(temp)).thenReturn(true);
        when (u1.rc.canSenseLocation(new MapLocation(4,6))).thenReturn(true);
        when (u1.rc.canSenseLocation(new MapLocation(5,6))).thenReturn(true);
        when (u1.rc.canSenseLocation(new MapLocation(6,6))).thenReturn(true);
        when (u1.rc.canSenseLocation(new MapLocation(5,4))).thenReturn(true);
        when (u1.rc.canSenseLocation(new MapLocation(6,5))).thenReturn(true);
        when (u1.rc.canSenseLocation(new MapLocation(4,4))).thenReturn(true);
        when (u1.rc.canSenseLocation(new MapLocation(5,4))).thenReturn(true);
        when (u1.rc.canSenseLocation(new MapLocation(6,4))).thenReturn(true);
        when (u1.rc.isReady()).thenReturn(true);
        when (u1.rc.canMove(Direction.SOUTH)).thenReturn(true);
        when (u1.rc.canMove(Direction.NORTH)).thenReturn(true);
        when (u1.rc.canMove(Direction.NORTHEAST)).thenReturn(true);
        when (u1.rc.canMove(Direction.EAST)).thenReturn(true);
        when (u1.rc.canMove(Direction.SOUTHEAST)).thenReturn(true);
        when (u1.rc.canMove(Direction.SOUTHWEST)).thenReturn(true);
        when (u1.rc.canMove(Direction.WEST)).thenReturn(true);
        when (u1.rc.canMove(Direction.NORTHWEST)).thenReturn(true);
        assertEquals(u1.tryMove(), true);
    }

    /*
    @Test
    public void testEndTurn() throws GameActionException {
        Unit u1 = Mockito.mock(Unit.class);
        u1.rc = Mockito.mock(RobotController.class);
        u1.endTurn();
    }*/
}
