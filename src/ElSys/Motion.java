package ElSys;

import ElSys.Enums.CabinDirection;

public class Motion extends Thread
{
  private final double MAX_SPEED = 0.2;
  private final double STOPPING_DISTANCE = 0.2;
  private double estimatedLocation;
  private double speed = 0;
  private Integer destination = 0;
  private FloorAlignment floorAlignment;
  private MotorControl motorControl;
  private CabinDirection cabinDirection = CabinDirection.STOPPED;
  
  public Motion(SimPhysLocation simPhysLocation)
  {
    floorAlignment = new FloorAlignment(simPhysLocation);
    motorControl = new MotorControl(simPhysLocation);
    estimatedLocation = (double) floorAlignment.getCurrentFloor();
    this.start();
  }
  
  public void run()
  {
    while(true)
    {
      if (cabinDirection != CabinDirection.STOPPED)
      {
        if(speed != MAX_SPEED) speed += 0.1;
        if(cabinDirection == CabinDirection.UP)
        {
          moveElevator(speed);
        }
        
        else
        {
          moveElevator(-speed);
        }
  
        if(floorAlignment.reachedEndOfShaft())
        {
          cabinDirection = CabinDirection.STOPPED;
          estimatedLocation = floorAlignment.getCurrentFloor();
        }
  
        if(Math.abs(estimatedLocation - destination) <= STOPPING_DISTANCE)
        {
          stopElevator();
          System.out.println(isAligned());
        }
      }
      
      else speed = 0;
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
    if(destination != null)
    {
      if (Math.abs(destination - estimatedLocation) <= STOPPING_DISTANCE)
      {
        this.destination = destination;
        return true;
      }
    }
    
    return false;
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
    alignWithFloor();
    cabinDirection = CabinDirection.STOPPED;
  }
  
  private void moveElevator(double distance)
  {
    motorControl.moveElevator(distance);
    estimatedLocation += distance;
  }
  
  private void alignWithFloor()
  {
    if (!floorAlignment.isAligned())
    {
      while (!floorAlignment.isAligned())
      {
        if (cabinDirection == CabinDirection.UP) moveElevator(0.1);
        
        else moveElevator(-0.1);
      }
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