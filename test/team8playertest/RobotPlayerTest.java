package team8playertest;

import team8player.RobotPlayer;
import battlecode.common.*;
import battlecode.common.RobotController;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotEquals;


public class RobotPlayerTest {

    @Test
    public void testRobotPlayerCreation() {
        RobotPlayer r1 = Mockito.mock(RobotPlayer.class);
        RobotPlayer r2 = Mockito.mock(RobotPlayer.class);
        assertNotEquals(r1, r2);
    }

    public void testGetType() {
        RobotController rc1 = Mockito.mock(RobotController.class);
        when(rc1.getType()).thenReturn(RobotType.MINER);
        assertEquals(rc1.getType(), RobotType.MINER);
    }
}