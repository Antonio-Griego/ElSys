package ElSys;

import ElSys.Enums.CabinDirection;

import java.math.BigDecimal;

import static ElSys.Enums.CabinDirection.STOPPED;

public class Motion extends Thread
{
  private final double MAX_SPEED = 0.2;
  private final double STOPPING_DISTANCE = 0.2;
  
  private BigDecimal estimatedLocation;
  private int currentFloor;
  private Integer destination = null;
  private FloorAlignment floorAlignment;
  private MotorControl motorControl;
  private CabinDirection cabinDirection = STOPPED;
  
  public Motion(SimPhysLocation simPhysLocation)
  {
    floorAlignment = new FloorAlignment(simPhysLocation);
    motorControl = new MotorControl(simPhysLocation);
    estimatedLocation = new BigDecimal("0.0");
    this.start();
  }
  
  public void run()
  {
    while(true)
    {
//      System.out.println(destination);
//      System.out.println("Current Floor: "+floorAlignment.getCurrentFloor());
//      System.out.println("Estimated Location: "+estimatedLocation.toString());
      try
      {
        Thread.sleep(1000);
      } catch (InterruptedException e)
      {
        e.printStackTrace();
      }
      
      currentFloor = floorAlignment.getCurrentFloor();
      
      if(destination == null)
      {
        cabinDirection = STOPPED;
      }
      
      else
      {
        if(destination > currentFloor) cabinDirection = CabinDirection.UP;
        else if(destination < currentFloor) cabinDirection = CabinDirection.DOWN;
        
        moveElevator();
        
        if(floorAlignment.reachedEndOfShaft())
        {
          stopElevator();
        }
      }
      
      //      if(destination != null)
//      {
//        if(destination > estimatedLocation.doubleValue())
//        {
//          cabinDirection = CabinDirection.UP;
//        }
//
//        else if(destination < estimatedLocation.doubleValue())
//        {
//          cabinDirection = CabinDirection.DOWN;
//        }
//
//        else
//        {
//          alignWithFloor();
//          cabinDirection = CabinDirection.STOPPED;
//        }
//      }
//
//      else cabinDirection = CabinDirection.STOPPED;
//
//      if (cabinDirection != CabinDirection.STOPPED)
//      {
//        if(speed.doubleValue() < MAX_SPEED) speed = speed.add(new BigDecimal("0.1"));
//
//        if(cabinDirection == CabinDirection.UP)
//        {
//          moveElevator(speed);
//        }
//
//        if(cabinDirection == CabinDirection.DOWN)
//        {
//          moveElevator(speed.negate());
//        }
//
//        if(floorAlignment.reachedEndOfShaft())
//        {
//          cabinDirection = CabinDirection.STOPPED;
//          estimatedLocation = new BigDecimal(Integer.toString(floorAlignment.getCurrentFloor()));
//        }
//
//        if(destination != null && estimatedLocation.subtract(new BigDecimal(Double.toString(destination))).abs().doubleValue() <= STOPPING_DISTANCE)
//        {
//          stopElevator();
//        }
//      }
//
//      else speed = new BigDecimal("0.0");
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
      if (cabinDirection == STOPPED ||
         (estimatedLocation.subtract(BigDecimal.valueOf(destination)).abs().signum() >= STOPPING_DISTANCE))
      {
        if(destination != this.destination) System.out.println("New Destination is Floor "+destination);
        this.destination = destination;
        return true;
      }

      //else System.out.println("Destination Rejected! Current Floor: "+floorAlignment.getCurrentFloor());
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
    return currentFloor;
  }

  synchronized public boolean isAligned()
  {
    return floorAlignment.isAligned();
  }

  synchronized public void stopElevator()
  {
    alignWithFloor();
    destination = null;
    cabinDirection = STOPPED;
  }

  private void moveElevator()
  {
    if(cabinDirection != STOPPED)
    {
      if (Math.abs(estimatedLocation.doubleValue() - destination) <= STOPPING_DISTANCE)
      {
        stopElevator();
      }

      else
      {
        BigDecimal distance = new BigDecimal(Double.toString(MAX_SPEED));
        if (cabinDirection == CabinDirection.DOWN) distance = distance.negate();
  
        motorControl.moveElevator(distance);
        estimatedLocation = estimatedLocation.add(distance);
    
      }
    }
  }

  private void alignWithFloor()
  {
    BigDecimal moveDistance = new BigDecimal("0.1");
    if (!floorAlignment.isAligned())
    {
      if (cabinDirection == CabinDirection.DOWN)
      {
        moveDistance = moveDistance.negate();
      }
      
      motorControl.moveElevator(moveDistance);
      estimatedLocation = estimatedLocation.add(moveDistance);
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