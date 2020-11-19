package team8playertest.robottest;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import org.junit.Test;
import org.mockito.Mockito;
import team8player.Robots.DesignSchool;
import static org.junit.Assert.*;

public class DesignSchoolTest {

    @Test
    public void testDesignCreation() {
        DesignSchool d1 = Mockito.mock(DesignSchool.class);
        DesignSchool d2 = Mockito.mock(DesignSchool.class);
        assertNotEquals(d1, d2);
    }

    @Test
    public void testRun() throws GameActionException {
        RobotController rc = Mockito.mock(RobotController.class);
        DesignSchool d1 = new DesignSchool(rc);
        d1.run();
    }
}
