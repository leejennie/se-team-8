package team8player.Robots;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import team8player.Blockchain;

import static team8player.Globals.*;

public class Building implements PlayerBot {

    /**
     * Robot constructor
     * @return a Building
     */
    public Building() {

    }

    @Override
    public void run() throws GameActionException {
        Blockchain.updateListsFromBC();
    }
}
