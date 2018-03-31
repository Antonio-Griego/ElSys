package ElSys;

public class SimPhysLocation
{
  private int currentHeightInFeet = -1;
  private int currentFloor = -1;

  public void move(int newFloor)
  {
    currentFloor = newFloor;
  }

  public int getPhysicalLocation()
  {
    return currentHeightInFeet;
  }

  public int getCurrentFloor()
  {
    return currentFloor;
  }
}