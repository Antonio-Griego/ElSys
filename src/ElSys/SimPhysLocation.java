package ElSys;

/**
 * This class stores the simulated location of an elevator.
 *
 * @author Antonio Griego
 */
public class SimPhysLocation
{
  private final int floors;
  private double location; // unit of distance = floors
  private int lastFloor;

  /**
   * Constructor method.
   *
   * @param floors The maximum number of floors in the building.
   */
  public SimPhysLocation(int floors)
  {
    this.floors = floors;
    this.location = 0.0;
    lastFloor = (int) location;
  }

  /**
   * Move the elevator the specified distance in floor units. For example,
   * move(0.5) moves the elevator up 1/2 of a floor and move(-2.0) moves the
   * elevator down two floors.
   *
   * If the distance moved would result in the location being out of bounds,
   * the value is truncated so that it simply moves the elevator the maximum
   * distance allowed. For example, when on the ground floor, move(1337.0)
   * will move the elevator to the top floor (the 10th floor) and location
   * will equal 9.0.
   *
   * @param distance The distance in floor units to move the elevator.
   */
  public synchronized void move(double distance)
  {
    if(location + distance < 0)
    {
      location = 0.0;
    }
    else if((location + distance) > (floors - 1))
    {
      location = floors - 1;
    }
    else
    {
      location += distance;
    }
    
    printStatus();
  }

  /**
   * This method returns true if the elevator cabin is either at the TOP or
   * the BOTTOM of the elevator shaft.
   *
   * @return True : The elevator is at the top or bottom.
   *         False : The elevator is NOT at the top or bottom.
   */
  public synchronized boolean reachedEndOfShaft()
  {
    return (location == 0 || location == (double) floors-1);
  }

  /**
   * This method returns an integer representing the floor if is aligned with
   * a floor or -1 if it is somewhere in between floors.
   *
   * @return The current floor, or -1 if in between floors.
   */
  public synchronized int getAlignedFloor()
  {
    if(location - (int) location != 0) return -1;
    else return (int) location;
  }
  
  /**
   * For Debugging
   */
  private void printStatus()
  {
    int currentFloor = (int) (location + 0.5);
    if(currentFloor != lastFloor)
    {
      if(currentFloor < lastFloor) System.out.println("Elevator is going DOWN to floor "+currentFloor);
      else System.out.println("Elevator is going UP to floor "+currentFloor);
      lastFloor = currentFloor;
    }
  }
}