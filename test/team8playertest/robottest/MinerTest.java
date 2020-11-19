package team8playertest.robottest;

import team8player.Robots.Miner;
import battlecode.common.*;
import battlecode.common.RobotController;
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
        when(m1.rc.getType()).thenReturn(RobotType.MINER);
        when(m1.rc.isReady()).thenReturn(true);
        when(m1.rc.canMineSoup(Direction.SOUTH)).thenReturn(true);
        m1.tryMine(Direction.SOUTH);
    }

    @Test
    public void testTryMineFalse() throws GameActionException {
        Miner m1 = Mockito.mock(Miner.class);
        m1.rc = Mockito.mock(RobotController.class);
        when(m1.rc.getType()).thenReturn(RobotType.MINER);
        when(m1.rc.isReady()).thenReturn(false);
        when(m1.rc.canMineSoup(Direction.SOUTH)).thenReturn(false);
        m1.tryMine(Direction.SOUTH);
    }

    @Test
    public void testTryDeposit() throws GameActionException {
        Miner m1 = Mockito.mock(Miner.class);
        m1.rc = Mockito.mock(RobotController.class);
        when(m1.rc.getType()).thenReturn(RobotType.MINER);
        when(m1.rc.isReady()).thenReturn(true);
        when(m1.rc.canDepositSoup(Direction.SOUTH)).thenReturn(true);
        m1.tryDeposit(Direction.SOUTH);
    }

    @Test
    public void testTryDepositFalse() throws GameActionException {
        Miner m1 = Mockito.mock(Miner.class);
        m1.rc = Mockito.mock(RobotController.class);
        when(m1.rc.getType()).thenReturn(RobotType.MINER);
        when(m1.rc.isReady()).thenReturn(false);
        when(m1.rc.canDepositSoup(Direction.SOUTH)).thenReturn(false);
        m1.tryDeposit(Direction.SOUTH);
    }

    @Test
    public void testRun() throws GameActionException {
        RobotController rc = Mockito.mock(RobotController.class);
        Miner m1 = new Miner(rc);
        m1.run();
    }
}

