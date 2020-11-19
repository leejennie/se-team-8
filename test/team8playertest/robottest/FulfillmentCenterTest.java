package team8playertest.robottest;

import battlecode.common.*;
import org.junit.Test;
import org.mockito.Mockito;
import team8player.Robots.FulfillmentCenter;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FulfillmentCenterTest {

    @Test
    public void testFulfillmentCenterCreation() {
        FulfillmentCenter f1 = Mockito.mock(FulfillmentCenter.class);
        FulfillmentCenter f2 = Mockito.mock(FulfillmentCenter.class);
        assertNotEquals(f1, f2);
    }

    @Test
    public void testRun() throws GameActionException {
        RobotController rc = Mockito.mock(RobotController.class);
        FulfillmentCenter f1 = new FulfillmentCenter(rc);
        f1.run();
    }
}


