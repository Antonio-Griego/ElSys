package ElSys;

import ElSys.Enums.CabinDirection;
import ElSys.Enums.CabinMode;

public class CabinStatus
{
  private int currentFloor = -1;
  private CabinDirection cabinDirection = CabinDirection.NOT_MOVING;
  private CabinMode cabinMode = CabinMode.STANDARD;

  CabinStatus()
  {
  }

  public int getFloor()
  {
    return currentFloor;
  }

  public CabinDirection getDirection()
  {
    return cabinDirection;
  }

  public CabinMode getMode()
  {
    return cabinMode;
  }
}