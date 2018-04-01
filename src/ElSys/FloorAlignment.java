package ElSys;

public class FloorAlignment
{
  private SimPhysLocation simPhysLocation;

  FloorAlignment(SimPhysLocation simPhysLocation)
  {
    this.simPhysLocation = simPhysLocation;
  }

  public int getCurrentFloor()
  {
    return 0;
  }

  public int getDistanceToAlign()
  {
    return (int)simPhysLocation.getPhysicalLocation();
  }
}