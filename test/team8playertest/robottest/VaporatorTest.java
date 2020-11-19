package team8playertest.robottest;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import org.junit.Test;
import org.mockito.Mockito;
import team8player.Robots.Vaporator;
import static org.junit.Assert.*;

public class VaporatorTest {
    @Test
    public void testVaporatorCreation() {
        Vaporator v1 = Mockito.mock(Vaporator.class);
        Vaporator v2 = Mockito.mock(Vaporator.class);
        assertNotEquals(v1, v2);
    }

    @Test
    public void testRun() throws GameActionException {
        RobotController rc = Mockito.mock(RobotController.class);
        Vaporator v1 = new Vaporator(rc);
        v1.run();
    }
}
