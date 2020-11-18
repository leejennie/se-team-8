package team8playertest.robottest;

import team8player.Robots.DeliveryDrone;
import team8player.Robots.Miner;
import battlecode.common.*;
import battlecode.common.RobotController;
import team8player.Globals.*;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MinerTest {

    @Test
    public void testMinerCreation() {
        Miner m1 = Mockito.mock(Miner.class);
        Miner m2 = Mockito.mock(Miner.class);
        assertNotEquals(m1, m2);
    }

    @Test
    public void testTryMine() throws GameActionException {
        Miner m1 = Mockito.mock(Miner.class);
        m1.rc = Mockito.mock(RobotController.class);
        if (m1.rc.getType() == RobotType.MINER){
            when(m1.tryMine(Direction.SOUTH)).thenReturn(true);
            assertEquals(m1.tryMine(Direction.SOUTH), true);
        }
        m1.tryMine(Direction.SOUTH);
    }

    @Test
    public void testTryDeposit() throws GameActionException {
        Miner m1 = Mockito.mock(Miner.class);
        m1.rc = Mockito.mock(RobotController.class);
        if (m1.rc.getType() == RobotType.MINER){
            when(m1.tryDeposit(Direction.SOUTH)).thenReturn(true);
            assertEquals(m1.tryDeposit(Direction.SOUTH), true);
        }
        m1.tryDeposit(Direction.SOUTH);
    }

    @Test // Need to fix this
    public void testRun() throws GameActionException {
        Miner m1 = Mockito.mock(Miner.class);
        m1.rc = Mockito.mock(RobotController.class);
        if (m1.rc.getType() == RobotType.MINER) {
            doNothing().when(m1).run();
            verify(m1, times(1)).run();
        }
        m1.run();
    }
}

