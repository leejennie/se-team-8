package team8playertest.robottest;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import org.junit.Test;
import org.mockito.Mockito;
import team8player.Robots.Refinery;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RefineryTest {

    @Test
    public void testRefineryCreation() {
        Refinery r1 = Mockito.mock(Refinery.class);
        Refinery r2 = Mockito.mock(Refinery.class);
        assertNotEquals(r1, r2);
    }

    @Test
    public void testRun() throws GameActionException {
        RobotController rc = Mockito.mock(RobotController.class);
        Refinery r1 = new Refinery(rc);
        r1.run();
    }

}
