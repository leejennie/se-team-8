package team8player.Robots;

import team8player.*;
import battlecode.common.*;

public class Building extends PlayerBot {

    /**
     * Robot constructor
     * @return a Building
     */
    public Building(RobotController rc) {
        super(rc);
    }

    @Override
    public void run() throws GameActionException {
        Blockchain.updateListsFromBC();
    }
}
