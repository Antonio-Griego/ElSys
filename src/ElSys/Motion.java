package ElSys;

import ElSys.Enums.CabinDirection;

public class Motion extends Thread
{
  private final double MAX_SPEED = 0.2;
  private final double STOPPING_DISTANCE = 0.1;
  private double estimatedLocation;
  private double speed;
  private Integer destination;
  private FloorAlignment floorAlignment;
  private MotorControl motorControl;
  private CabinDirection cabinDirection = CabinDirection.STOPPED;
  
  public Motion(SimPhysLocation simPhysLocation)
  {
    estimatedLocation = (double) floorAlignment.getAlignedFloor();
    floorAlignment = new FloorAlignment(simPhysLocation);
    motorControl = new MotorControl(simPhysLocation);
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
          estimatedLocation += MAX_SPEED;
        }
        
        else
        {
          motorControl.moveElevator(-MAX_SPEED);
          estimatedLocation -= MAX_SPEED;
        }
      }
      
      if(floorAlignment.reachedEndOfShaft())
      {
        cabinDirection = CabinDirection.STOPPED;
        estimatedLocation = floorAlignment.getAlignedFloor();
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

  synchronized public boolean setDestination(final Integer destination)
  {
    this.destination = destination;
    return true;
  }

  synchronized public Integer getDestination()
  {
    return destination;
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
    alignWithFloor(floorAlignment.getCurrentFloor()+1);
    cabinDirection = CabinDirection.STOPPED;
  }
  
  private void alignWithFloor(int floorToAlignWith)
  {
    while(floorAlignment.getAlignedFloor() == -1)
    {
    
    }
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