package ElSys;

public class FloorAlignment
{
  private SimPhysLocation simPhysLocation;
  private int floor, MAX = 9, MIN = 0;

  FloorAlignment(SimPhysLocation simPhysLocation)
  {
    this.simPhysLocation = simPhysLocation;
  }

  public int getCurrentFloor()
  {
    // makes the double to int cast with the appropriate rounding.
    floor = (int) (simPhysLocation.getPhysicalLocation()+.5);
    return floor;
  }

  public double getDistanceToAlign()
  {
    floor = getCurrentFloor();
    return (floor - simPhysLocation.getPhysicalLocation());
    // returns positive and negative values, not absolute value of distance
  }

  public boolean reachedEndOfShaft()
  {
    // return simPhysLocation.reachedEndOfShaft();

    // the above code will work once simPhysLocation has reachedEndOfShaft method,
    // if not, the below code will work.

    floor = getCurrentFloor();
    if (floor == MAX || floor == MIN)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
}