package team8playertest.robottest;

import team8player.Robots.Miner;
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

    @Test
    public void testTryMove1() throws GameActionException {
        Unit u1 = Mockito.mock(Unit.class);
        u1.rc = Mockito.mock(RobotController.class);
        if (u1.rc.getType() == RobotType.MINER){
            when(u1.tryMove()).thenReturn(true);
            assertEquals(u1.tryMove(), true);
        }
        u1.tryMove();
    }

    @Test
    public void testTryMove() throws GameActionException {
        Unit u1 = Mockito.mock(Unit.class);
        u1.rc = Mockito.mock(RobotController.class);
        if (u1.rc.getType() == RobotType.MINER){
            when(u1.tryMove(Direction.SOUTH)).thenReturn(true);
            assertEquals(u1.tryMove(Direction.SOUTH), true);
        }
        u1.tryMove(Direction.SOUTH);
    }

    @Test // Need to fix this
    public void testEndTurn() throws GameActionException {
        Unit u1 = Mockito.mock(Unit.class);
        u1.rc = Mockito.mock(RobotController.class);
        if (u1.rc.getType() == RobotType.MINER) {
            doNothing().when(u1).endTurn();
            verify(u1, times(1)).endTurn();
        }
        u1.endTurn();
    }

}
