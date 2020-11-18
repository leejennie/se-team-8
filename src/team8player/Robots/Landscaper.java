package team8player.Robots;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import static team8player.Globals.*;

import java.util.LinkedList;

public class Landscaper extends Unit {

    /**
     * Robot constructor
     * @return a random RobotType
     */
    public Landscaper() {
    }

    @Override
    public void run() throws GameActionException {
        super.run();
        currentGoal=HqLocation;
        int currDirt = rc.getDirtCarrying();
        if (currDirt == 0) {
            Direction dir = PlayerBot.randomDirection();
            if (tryDig(dir)) {
                System.out.println("I dug in the " + dir + " direction.");
                tryMove(rc.getLocation().directionTo(currentGoal));
                return;
            }

        }
        else if (currDirt == 25) {
            Direction dir;
            if((rc.getLocation().x==currentGoal.x+1)&&(rc.getLocation().y==currentGoal.y)) {
                dir = Direction.SOUTH;
                if (tryDepositDirt(dir)) {
                    System.out.println("I deposited dirt in the " + dir + " direction.");
                    tryMove(dir);
                    return;
                }
            }
            else if((rc.getLocation().x==currentGoal.x+1)&&(rc.getLocation().y==currentGoal.y-1)) {
                dir = Direction.WEST;
                if (tryDepositDirt(dir)) {
                    System.out.println("I deposited dirt in the " + dir + " direction.");
                    tryMove(dir);
                    return;
                }
            }
            else if((rc.getLocation().x==currentGoal.x)&&(rc.getLocation().y==currentGoal.y-1)) {
                dir = Direction.WEST;
                if (tryDepositDirt(dir)) {
                    System.out.println("I deposited dirt in the " + dir + " direction.");
                    tryMove(dir);
                    return;
                }
            }
            else if((rc.getLocation().x==currentGoal.x-1)&&(rc.getLocation().y==currentGoal.y-1)) {
                dir = Direction.NORTH;
                if (tryDepositDirt(dir)) {
                    System.out.println("I deposited dirt in the " + dir + " direction.");
                    tryMove(dir);
                    return;
                }
            }
            else if((rc.getLocation().x==currentGoal.x-1)&&(rc.getLocation().y==currentGoal.y)) {
                dir = Direction.NORTH;
                if (tryDepositDirt(dir)) {
                    System.out.println("I deposited dirt in the " + dir + " direction.");
                    tryMove(dir);
                    return;
                }
            }
            else if((rc.getLocation().x==currentGoal.x-1)&&(rc.getLocation().y==currentGoal.y+1)) {
                dir = Direction.EAST;
                if (tryDepositDirt(dir)) {
                    System.out.println("I deposited dirt in the " + dir + " direction.");
                    tryMove(dir);
                    return;
                }
            }
            else if((rc.getLocation().x==currentGoal.x)&&(rc.getLocation().y==currentGoal.y+1)) {
                dir = Direction.EAST;
                if (tryDepositDirt(dir)) {
                    System.out.println("I deposited dirt in the " + dir + " direction.");
                    tryMove(dir);
                    return;
                }
            }
            else if((rc.getLocation().x==currentGoal.x+1)&&(rc.getLocation().y==currentGoal.y+1)) {
                dir = Direction.SOUTH;
                if (tryDepositDirt(dir)) {
                    System.out.println("I deposited dirt in the " + dir + " direction.");
                    tryMove(dir);
                    return;
                }
            }

            tryMove(rc.getLocation().directionTo(currentGoal));
        }

        else {
            for (Direction dir : Direction.allDirections())
                if (tryDig(dir)) {
                    System.out.println("I dug in the " + dir + " direction.");
                    tryMove(rc.getLocation().directionTo(currentGoal));
                    return;
                }
            Direction dir;
            if((rc.getLocation().x==currentGoal.x+1)&&(rc.getLocation().y==currentGoal.y)) {
                dir = Direction.SOUTH;
                if (tryDepositDirt(dir)) {
                    System.out.println("I deposited dirt in the " + dir + " direction.");
                    tryMove(dir);
                    return;
                }
            }
            else if((rc.getLocation().x==currentGoal.x+1)&&(rc.getLocation().y==currentGoal.y-1)) {
                dir = Direction.WEST;
                if (tryDepositDirt(dir)) {
                    System.out.println("I deposited dirt in the " + dir + " direction.");
                    tryMove(dir);
                    return;
                }
            }
            else if((rc.getLocation().x==currentGoal.x)&&(rc.getLocation().y==currentGoal.y-1)) {
                dir = Direction.WEST;
                if (tryDepositDirt(dir)) {
                    System.out.println("I deposited dirt in the " + dir + " direction.");
                    tryMove(dir);
                    return;
                }
            }
            else if((rc.getLocation().x==currentGoal.x-1)&&(rc.getLocation().y==currentGoal.y-1)) {
                dir = Direction.NORTH;
                if (tryDepositDirt(dir)) {
                    System.out.println("I deposited dirt in the " + dir + " direction.");
                    tryMove(dir);
                    return;
                }
            }
            else if((rc.getLocation().x==currentGoal.x-1)&&(rc.getLocation().y==currentGoal.y)) {
                dir = Direction.NORTH;
                if (tryDepositDirt(dir)) {
                    System.out.println("I deposited dirt in the " + dir + " direction.");
                    tryMove(dir);
                    return;
                }
            }
            else if((rc.getLocation().x==currentGoal.x-1)&&(rc.getLocation().y==currentGoal.y+1)) {
                dir = Direction.EAST;
                if (tryDepositDirt(dir)) {
                    System.out.println("I deposited dirt in the " + dir + " direction.");
                    tryMove(dir);
                    return;
                }
            }
            else if((rc.getLocation().x==currentGoal.x)&&(rc.getLocation().y==currentGoal.y+1)) {
                dir = Direction.EAST;
                if (tryDepositDirt(dir)) {
                    System.out.println("I deposited dirt in the " + dir + " direction.");
                    tryMove(dir);
                    return;
                }
            }
            else if((rc.getLocation().x==currentGoal.x+1)&&(rc.getLocation().y==currentGoal.y+1)) {
                dir = Direction.SOUTH;
                if (tryDepositDirt(dir)) {
                    System.out.println("I deposited dirt in the " + dir + " direction.");
                    tryMove(dir);
                    return;
                }
            }
        }
        endTurn();
    }

    /**
     * Attempts to dig in a given direction
     *
     * @param dir The intended direction to dig
     * @return true if it dug
     * @throws GameActionException
     */
    static boolean tryDig(Direction dir) throws GameActionException {
        if (rc.canDigDirt(dir)) {
            rc.digDirt(dir);
            return true;
        }
        return false;
    }

    /**
     * Attempts to deposit dirt in a given direction
     *
     * @param dir The intended direction to deposit
     * @return true if deposited
     * @throws GameActionException
     */
    static boolean tryDepositDirt(Direction dir) throws GameActionException {
        if(rc.canDepositDirt(dir)) {
            rc.depositDirt(dir);
            return true;
        }
        return false;
    }
}
