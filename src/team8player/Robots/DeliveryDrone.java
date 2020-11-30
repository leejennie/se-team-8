package team8player.Robots;

import battlecode.common.*;

import static team8player.Globals.*;
import static team8player.Globals.STR_PHS_DESTROY;

public class DeliveryDrone extends Unit {

    RobotInfo goalBot = null;

    /**
     * Robot constructor
     * @return a random RobotType
     */
    public DeliveryDrone(RobotController rc) {
        super(rc);
    }

    public void expandPhase() throws GameActionException {

        switch (selfPhase) {
            case DD_PHS_DEF:
                // If enemy bot is found, update location and try to attack/carry if able
                for(RobotInfo bot: nearbyBots) {
                    if(bot.team != rc.getTeam()) {
                        if(bot.type.canBePickedUp()) {
                            goalBot = bot;
                            currentGoal = bot.location;
                            selfPhase = DD_PHS_PKUP_E;
                            break;
                        }
                    }
                }
                // ignoring cows since we are going for a rushing strategy
                break;
            case DD_PHS_PKUP_A:
                // keep moving toward goal location until can pick up bot at location
                if(rc.canPickUpUnit(rc.senseRobotAtLocation(currentGoal).ID)) {
                    currentGoal = null;
                    rc.pickUpUnit(rc.senseRobotAtLocation(currentGoal).ID);
                    selfPhase = DD_PHS_DRP;
                    for(MapLocation ref: refineries) {
                        if(currentGoal == null) {
                            currentGoal = ref;
                            continue;
                        }
                        if(currentLoc.distanceSquaredTo(ref) < currentLoc.distanceSquaredTo(currentGoal))
                            currentGoal = ref;
                    }
                }
                break;

            case DD_PHS_PKUP_E:
                // if can pick up enemy, do so and try to destroy it
                if(rc.canPickUpUnit(goalBot.ID)) {
                    rc.pickUpUnit(goalBot.ID);
                    selfPhase = DD_PHS_KILL;
                    // try to sense nearby water, set goal to last seen water if not found, then search if neither found
                    boolean water_found = false;
                    for(int x = -10; x <= 10; x++)
                        for(int y = -10; y <=10; y++)
                            if(rc.onTheMap(new MapLocation(x + currentLoc.x, y + currentLoc.y)))
                                if(rc.senseFlooding(new MapLocation(x + currentLoc.x, y + currentLoc.y))) {
                                    currentGoal = new MapLocation(x + currentLoc.x, y + currentLoc.y);
                                    water_found = true;
                                    selfPhase = DD_PHS_KILL;
                                    break;
                                }
                    if(water_found == false) {
                        if (lastSeenWater != null) {
                            currentGoal = lastSeenWater;
                            selfPhase = DD_PHS_KILL;
                        }
                        else {
                            // Start to search for water
                            selfPhase = DD_PHS_WATER;
                        }
                    }
                }
                boolean bot_found = false;
                for(RobotInfo bot: nearbyBots) {
                    if (bot.ID == goalBot.ID) {
                        bot_found = true;
                        break;
                    }
                }
                if(!bot_found)
                    selfPhase = DD_PHS_DEF;
                break;
            case DD_PHS_PKUP_C:
                // skipping picking up cows
                break;
            case DD_PHS_DRP:
                //check if close to refinery
                if(currentGoal.distanceSquaredTo(currentLoc) < DROP_OFF_RADIUS) {
                    if (rc.canDropUnit(currentLoc.directionTo(currentGoal))) {
                        rc.dropUnit(currentLoc.directionTo(currentGoal));
                        selfPhase = DD_PHS_DEF;
                    }
                    else for(Direction dir: Direction.allDirections())
                        if(rc.canDropUnit(dir)) {
                            rc.dropUnit(dir);
                            selfPhase = DD_PHS_DEF;
                        }
                }
                break;
            case DD_PHS_KILL:
                if(rc.senseFlooding(rc.getLocation().add(currDirection))) {
                    rc.dropUnit(currDirection);
                    selfPhase = DD_PHS_DEF;
                }
                break;
            case DD_PHS_WATER:
                boolean water_found = false;
                for(int x = -10; x <= 10; x++)
                    for(int y = -10; y <=10; y++)
                        if(rc.onTheMap(new MapLocation(x + currentLoc.x, y + currentLoc.y)))
                            if(rc.senseFlooding(new MapLocation(x + currentLoc.x, y + currentLoc.y))) {
                                currentGoal = new MapLocation(x + currentLoc.x, y + currentLoc.y);
                                selfPhase = DD_PHS_KILL;
                                break;
                            }
            default:
                break;
        }


        // Explore and gather info if nothing else to do
    }

    public void searchPhase() {

    }

    public void destroyPhase() {

    }

    @Override
    public void run() throws GameActionException {

        super.run();

        if(rc.senseFlooding(currentLoc))
            lastSeenWater = currentLoc;

        switch (stratPhase) {
            case STR_PHS_EXPAND:
                expandPhase();
                break;
            case STR_PHS_SEARCH:
                searchPhase();
                break;
            case STR_PHS_DESTROY:
                destroyPhase();
                break;
            default:
                break;
        }

        Team enemy = rc.getTeam().opponent();
        if (!rc.isCurrentlyHoldingUnit()) {
            // See if there are any enemy robots within capturing range
            RobotInfo[] robots = rc.senseNearbyRobots(GameConstants.DELIVERY_DRONE_PICKUP_RADIUS_SQUARED, enemy);

            if (robots.length > 0) {
                // Pick up a first robot within range
                rc.pickUpUnit(robots[0].getID());
                System.out.println("I picked up " + robots[0].getID() + "!");
            }
        } else {
            // No close robots, so search for robots within sight radius
            Unit.tryMove(PlayerBot.randomDirection());
        }
    }
}
