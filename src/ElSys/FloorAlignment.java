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
    return simPhysLocation.getCurrentFloor();
  }

  public int getDistanceToAlign()
  {
    return simPhysLocation.getPhysicalLocation();
  }
}