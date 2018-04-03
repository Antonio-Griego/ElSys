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
    this.start();
  }
  
  public void run()
  {
    while(true)
    {
      try{
        Thread.sleep(500);
      } catch (InterruptedException e)
      {
        e.printStackTrace();
      }
      
      if (cabinDirection != CabinDirection.STOPPED)
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
      
      if(floorAlignment.reachedEndOfShaft())
      {
        cabinDirection = CabinDirection.STOPPED;
      }
      
      //      stillRunning();
    }
  }
  
  synchronized public void setDirection(CabinDirection cabinDirection)
  {
    this.cabinDirection = cabinDirection;
  }

  synchronized public CabinDirection getDirection()
  {
    return this.cabinDirection;
  }

  synchronized public int getFloor()
  {
    return floorAlignment.getCurrentFloor();
  }

  synchronized public boolean isAligned()
  {
    return (floorAlignment.getDistanceToAlign() == 0);
  }
  
  synchronized public void stopElevator()
  {
    alignWithFloor();
    cabinDirection = CabinDirection.STOPPED;
  }
  
  private void alignWithFloor()
  {
    motorControl.moveElevator(floorAlignment.getDistanceToAlign());
  }
  
  /**
   * For debugging
   */
  private double lastCheck = 0;
  private void stillRunning()
  {
    double currentTime = System.currentTimeMillis();
    if(lastCheck == 0)
    {
      lastCheck = currentTime;
      System.out.println("Motor Running");
    }
    
    if(currentTime - lastCheck >= 5000)
    {
      System.out.println("Motor Running");
      lastCheck = currentTime;
    }
  }
}