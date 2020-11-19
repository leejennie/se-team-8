package team8playertest.robottest;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import org.junit.Test;
import org.mockito.Mockito;
import team8player.Robots.NetGun;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NetGunTest {

    @Test
    public void testNetGunCreation() {
        NetGun n1 = Mockito.mock(NetGun.class);
        NetGun n2 = Mockito.mock(NetGun.class);
        assertNotEquals(n1, n2);
    }

    @Test
    public void testRun() throws GameActionException {
        RobotController rc = Mockito.mock(RobotController.class);
        NetGun n1 = new NetGun(rc);

        n1.run();
    }

}
