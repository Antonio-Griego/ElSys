package ElSys;

import ElSys.Enums.CabinDirection;

public class Motion extends Thread
{
  private final double MAX_SPEED = 0.2;
  private boolean move;
  private FloorAlignment floorAlignment;
  private MotorControl motorControl;
  private CabinDirection cabinDirection = CabinDirection.STOPPED;

  
  
  Motion(SimPhysLocation simPhysLocation)
  {
    floorAlignment = new FloorAlignment(simPhysLocation);
    motorControl = new MotorControl(simPhysLocation);
    move = true;
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
    move = false;
    alignWithFloor();
  }
  
  public void moveElevator()
  {
    move = true;
  }
  
  public void run()
  {
    if(move && cabinDirection != CabinDirection.STOPPED)
    {
      motorControl.moveElevator(MAX_SPEED);
    }
  }
  
  private void alignWithFloor()
  {
    motorControl.moveElevator(floorAlignment.getDistanceToAlign());
  }
}