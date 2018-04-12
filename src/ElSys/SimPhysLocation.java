package ElSys;

import java.math.BigDecimal;

/**
 * This class stores the simulated location of an elevator.
 *
 * @author Antonio Griego
 */
public class SimPhysLocation
{
  private final int floors;
  private BigDecimal location; // unit of distance = floors
  private int lastFloor; //For Debugging

  /**
   * Constructor method.
   *
   * @param floors The maximum number of floors in the building.
   */
  public SimPhysLocation(int floors)
  {
    this.floors = floors;
    this.location = new BigDecimal("0.00");
    location.setScale(20);
    lastFloor = location.intValue();
  }

  /**
   * Move the elevator the specified distance in floor units. There are 10 floor units in one floor.
   * For example, move(5) moves the elevator up 1/2 of a floor and move(-20) moves the
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
  public synchronized void move(BigDecimal distance)
  {
    if(location.add(distance).doubleValue() < 0)
    {
      location = new BigDecimal(0);
    }
    else if((location.add(distance)).doubleValue() > floors-1)
    {
      location = new BigDecimal(floors-1);
    }
    else
    {
      location = location.add(distance);
    }
    location.stripTrailingZeros();
    //System.out.println(location.toString());
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
    return (location.signum() == 0 || location.signum() == floors);
  }

  /**
   * This method returns an integer representing the floor if is aligned with
   * a floor or -1 if it is somewhere in between floors.
   *
   * @return The current floor, or -1 if in between floors.
   */
  public synchronized Integer getAlignedFloor()
  {
    if(location.doubleValue() - location.intValue() >= 0.01) return null;
    else return location.stripTrailingZeros().intValue();
  }
  
  /**
   * For Debugging
   */
  private void printStatus()
  {
    int currentFloor = location.setScale(0, BigDecimal.ROUND_UP).intValue();
    if(currentFloor != lastFloor)
    {
      if(currentFloor < lastFloor) System.out.println("Elevator is on floor "+currentFloor+" going DOWN");
      else System.out.println("Elevator is on floor "+currentFloor+" going UP");
      lastFloor = currentFloor;
    }
  }
}