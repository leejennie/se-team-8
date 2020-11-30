package team8playertest;

import team8player.Globals;
import battlecode.common.GameActionException;
import org.junit.Test;
import battlecode.common.*;
import battlecode.common.RobotController;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class GlobalsTest {

    @Test
    public void testIsUnit() throws GameActionException {
        Globals g1 = Mockito.mock(Globals.class);
        g1.rc = Mockito.mock(RobotController.class);

        when (g1.rc.getType()).thenReturn(RobotType.MINER);
        assertEquals(g1.isUnit(RobotType.MINER), true);
        g1.isUnit(RobotType.MINER);
    }

    @Test
    public void testIsUnitFalse() throws GameActionException {
        Globals g1 = Mockito.mock(Globals.class);
        g1.rc = Mockito.mock(RobotController.class);

        when (g1.rc.getType()).thenReturn(RobotType.DESIGN_SCHOOL);
        assertEquals(g1.isUnit(RobotType.DESIGN_SCHOOL), false);
        g1.isUnit(RobotType.DESIGN_SCHOOL);
    }
}
