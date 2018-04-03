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
  public void move(double distance)
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
   * This method returns the physical location of the elevator in floor units,
   * i.e., a value of 0.0 indicates the elevator is on floor 1 and 5.5
   * indicates the elevator is on floor 6.5 (halfway between floor 6 and 7).
   *
   * @return The physical location of the elevator in floor units in the range
   *         of [0.0, (N-1)] (for an N story building).
   */
  public double getPhysicalLocation()
  {
    return location;
  }
  
  public boolean reachedEndOfShaft()
  {
    return (location == 0 || location == (double) floors-1);
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