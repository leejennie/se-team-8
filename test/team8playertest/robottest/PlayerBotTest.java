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
    @Test // Need to fix
    public void testTryBuild() throws GameActionException {
        PlayerBot p1 = Mockito.mock(PlayerBot.class);
        p1.rc = Mockito.mock(RobotController.class);
        when (p1.rc.getType()).thenReturn(RobotType.MINER);
        assertEquals (p1.tryBuild(RobotType.DESIGN_SCHOOL), true);
        p1.tryBuild(RobotType.DESIGN_SCHOOL);
    }*/
}
