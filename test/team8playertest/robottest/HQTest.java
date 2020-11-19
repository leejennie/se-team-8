package team8playertest.robottest;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import org.junit.Test;
import org.mockito.Mockito;
import team8player.Robots.HQ;
import static org.junit.Assert.*;

public class HQTest {
    @Test
    public void testFulfillmentCenterCreation() {
        HQ h1 = Mockito.mock(HQ.class);
        HQ h2 = Mockito.mock(HQ.class);
        assertNotEquals(h1, h2);
    }

    @Test
    public void testRun() throws GameActionException {
        RobotController rc = Mockito.mock(RobotController.class);
        HQ h1 = new HQ(rc);
        h1.run();
    }
}
