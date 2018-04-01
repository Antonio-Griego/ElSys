package ElSys;

import ElSys.Enums.CabinDirection;
import ElSys.Enums.CabinMode;

public class CabinStatus
{
  private final int currentFloor;
  private final CabinDirection cabinDirection;
  private final CabinMode cabinMode;

  CabinStatus(final int currentFloor, final CabinDirection cabinDirection, final CabinMode cabinMode)
  {
    this.currentFloor = currentFloor;
    this.cabinDirection = cabinDirection;
    this. cabinMode = cabinMode;
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