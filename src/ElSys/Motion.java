package ElSys;

import ElSys.Enums.CabinDirection;

public class Motion extends Thread
{
  private final double MAX_SPEED = 0.2;
  private boolean moving;
  private FloorAlignment floorAlignment;
  private MotorControl motorControl;
  private CabinDirection cabinDirection = CabinDirection.STOPPED;
  
  public Motion(SimPhysLocation simPhysLocation)
  {
    floorAlignment = new FloorAlignment(simPhysLocation);
    motorControl = new MotorControl(simPhysLocation);
    moving = false;
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

  public boolean isAligned()
  {
    return (floorAlignment.getDistanceToAlign() == 0);
  }
  
  public void stopElevator()
  {
    moving = false;
    alignWithFloor();
  }
  
  public void run()
  {
    while(true)
    {
      if (moving && cabinDirection != CabinDirection.STOPPED)
      {
        if(cabinDirection == CabinDirection.UP)
        {
          motorControl.moveElevator(MAX_SPEED);
        }
        
        else
        {
          motorControl.moveElevator(-MAX_SPEED);
        }
      }
    }
  }
  
  private void alignWithFloor()
  {
    motorControl.moveElevator(floorAlignment.getDistanceToAlign());
  }
}