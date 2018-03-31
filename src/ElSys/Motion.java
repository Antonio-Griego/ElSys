package ElSys;

import ElSys.Enums.CabinDirection;

public class Motion
{
  private FloorAlignment floorAlignment;
  private MotorControl motorControl;
  private CabinDirection cabinDirection = CabinDirection.NOT_MOVING;

  Motion(SimPhysLocation simPhysLocation)
  {
    floorAlignment = new FloorAlignment(simPhysLocation);
    motorControl = new MotorControl(simPhysLocation);
  }

  public void setDirection(CabinDirection cabinDirection)
  {
    this.cabinDirection = cabinDirection;
  }

  public CabinDirection getDirection()
  {
    return this.cabinDirection;
  }

  public int getFloor()
  {
    return floorAlignment.getCurrentFloor();
  }

  public void stop()
  {
    System.out.println("STOP!");
  }

  public boolean isAligned()
  {
    return false;
  }
}