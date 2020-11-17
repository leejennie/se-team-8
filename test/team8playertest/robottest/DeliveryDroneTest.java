package team8playertest.robottest;

import team8player.Robots.DeliveryDrone;
import battlecode.common.*;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DeliveryDroneTest {

    @Test
    public void testDeliveryDroneCreation() {
        DeliveryDrone dd1 = Mockito.mock(DeliveryDrone.class);
        DeliveryDrone dd2 = Mockito.mock(DeliveryDrone.class);
        assertNotEquals(dd1, dd2);
    }

    @Test
    public void testRun() throws GameActionException {
        DeliveryDrone dd = Mockito.mock(DeliveryDrone.class);
        dd.rc = Mockito.mock(RobotController.class);
        if (dd.rc.getType() == RobotType.DELIVERY_DRONE) {
            doNothing().when(dd).run();
            verify(dd, times(1)).run();
        }
        dd.run();
    }
}
