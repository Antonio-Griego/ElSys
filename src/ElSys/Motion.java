package ElSys;

import ElSys.Enums.CabinDirection;

import java.math.BigDecimal;

public class Motion extends Thread
{
  private final double MAX_SPEED = 0.2;
  private final double STOPPING_DISTANCE = 0.2;
  private BigDecimal estimatedLocation;
  private BigDecimal speed;
  private Integer destination = null;
  private FloorAlignment floorAlignment;
  private MotorControl motorControl;
  private CabinDirection cabinDirection = CabinDirection.STOPPED;
  
  public Motion(SimPhysLocation simPhysLocation)
  {
    floorAlignment = new FloorAlignment(simPhysLocation);
    motorControl = new MotorControl(simPhysLocation);
    estimatedLocation = new BigDecimal("0.0");
    speed = new BigDecimal("0.0");
    this.start();
  }
  
  public void run()
  {
    while(true)
    {
//      System.out.println(destination);
//      System.out.println("Current Floor: "+floorAlignment.getCurrentFloor());
//      System.out.println("Estimated Location: "+estimatedLocation.toString());

      if(destination != null)
      {
        if(destination > estimatedLocation.signum())
        {
          cabinDirection = CabinDirection.UP;
        }

        else if(destination < estimatedLocation.signum())
        {
          cabinDirection = CabinDirection.DOWN;
        }

        else cabinDirection = CabinDirection.STOPPED;
      }

      else cabinDirection = CabinDirection.STOPPED;

      if (cabinDirection != CabinDirection.STOPPED)
      {
        if(speed.doubleValue() < MAX_SPEED) speed = speed.add(new BigDecimal("0.1"));

        if(cabinDirection == CabinDirection.UP)
        {
          moveElevator(speed);
        }

        if(cabinDirection == CabinDirection.DOWN)
        {
          moveElevator(speed.negate());
        }

        if(floorAlignment.reachedEndOfShaft())
        {
          cabinDirection = CabinDirection.STOPPED;
          estimatedLocation = new BigDecimal(Integer.toString(floorAlignment.getCurrentFloor()));
        }

        if(estimatedLocation.subtract(new BigDecimal(Double.toString(destination))).abs().doubleValue() <= STOPPING_DISTANCE)
        {
          stopElevator();
          destination = null;
        }
      }

      else speed = new BigDecimal("0.0");
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
      if (cabinDirection == CabinDirection.STOPPED ||
         (estimatedLocation.subtract(BigDecimal.valueOf(destination)).abs().signum() >= STOPPING_DISTANCE) ||
         (destination == this.destination))
      {
        if(destination != this.destination) System.out.println("New Destination is Floor "+destination);
        this.destination = destination;
        return true;
      }

      else System.out.println("Destination Rejected! Current Floor: "+floorAlignment.getCurrentFloor());
    }
    this.destination = destination;
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

  private void moveElevator(BigDecimal distance)
  {
    motorControl.moveElevator(distance);
    estimatedLocation = estimatedLocation.add(distance);
  }

  private void alignWithFloor()
  {
    if (!floorAlignment.isAligned())
    {
      while (!floorAlignment.isAligned())
      {
        if (cabinDirection == CabinDirection.UP) moveElevator(new BigDecimal("0.1"));

        else moveElevator(new BigDecimal("0.1").negate());
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