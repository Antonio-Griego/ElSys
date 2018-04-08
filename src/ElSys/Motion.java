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
    estimatedLocation = (double) floorAlignment.getCurrentFloor();
    floorAlignment = new FloorAlignment(simPhysLocation);
    motorControl = new MotorControl(simPhysLocation);
    this.start();
  }
  
  public void run()
  {
    while(true)
    {
      if (cabinDirection != CabinDirection.STOPPED)
      {
        if(cabinDirection == CabinDirection.UP)
        {
          moveElevator(MAX_SPEED);
        }
        
        else
        {
          moveElevator(-MAX_SPEED);
        }
      }
      
      if(floorAlignment.reachedEndOfShaft())
      {
        cabinDirection = CabinDirection.STOPPED;
        estimatedLocation = floorAlignment.getCurrentFloor();
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
    return floorAlignment.isAligned();
  }
  
  synchronized public void stopElevator()
  {
    alignWithFloor(floorAlignment.getCurrentFloor()+1);
    cabinDirection = CabinDirection.STOPPED;
  }
  
  private void moveElevator(double distance)
  {
    motorControl.moveElevator(distance);
    estimatedLocation += distance;
  }
  
  private void alignWithFloor(int floorToAlignWith)
  {
    while(!floorAlignment.isAligned())
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