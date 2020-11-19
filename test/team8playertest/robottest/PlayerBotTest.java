package team8playertest.robottest;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import org.junit.Test;
import org.mockito.Mockito;
import team8player.Robots.PlayerBot;

import java.awt.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PlayerBotTest {
    /*
    @Test
    public void testTryBuild1() throws GameActionException {
        PlayerBot p1 = Mockito.mock(PlayerBot.class);
        p1.rc = Mockito.mock(RobotController.class);
        doNothing().when(p1).tryBuild(RobotType.MINER);
        verify(p1, times(1)).tryBuild(RobotType.MINER);

        p1.tryBuild(RobotType.MINER);
    }*/

    @Test
    public void testTryBuild1() throws GameActionException {
        PlayerBot p1 = Mockito.mock(PlayerBot.class);
        p1.rc = Mockito.mock(RobotController.class);
        when(p1.tryBuild(RobotType.MINER)).thenReturn(true);
        assertEquals(p1.tryBuild(RobotType.MINER), true);

        p1.tryBuild(RobotType.MINER);
    }

}
