package ElSys;

import ElSys.Enums.ButtonLight;
import ElSys.Enums.CabinDirection;
import ElSys.Enums.CabinMode;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Abstraction which represents a physical cabin, including the cabin's status, the requests that the cabin is tasked with
 * and the cabin's motion.
 *
 * This class is also responsible for the logic required in processing requests and determining which request to proceed
 * towards first.
 */
public class Cabin extends Thread
{
  private final CabinRequests cabinRequests;
  private final Motion motion;
  private final Set<FloorRequest> requests = new HashSet<>(); // uses set uniqueness to stop duplicate requests.

  private boolean hasArrived;
  private CabinMode cabinMode;
  private CabinStatus cabinStatus;

  /**
   * Create a new cabin with {@code numberOfFloors} floors using the {@link SimPhysLocation} specified.
   *
   * @param simButtons the simulated buttons that will be used.
   * @param simPhysLocation {@link SimPhysLocation} that will be used.
   */
  public Cabin(final SimButton[] simButtons, final SimPhysLocation simPhysLocation)
  {
    this.cabinRequests = new CabinRequests(simButtons);
    motion = new Motion(simPhysLocation);
    cabinMode = CabinMode.NORMAL;
    cabinStatus = new CabinStatus(motion.getFloor(),
                                  motion.getDirection(),
                                  CabinMode.NORMAL,
                                  requests,
                                  motion.getDestination());
  }


  public void updateRequests()
  {
    requests.clear();
    requests.addAll(cabinRequests.updateRequests());
  }
  /**
   * Based on current mode, updates state based on current requests.
   *
   * <p>When the cabin is in the normal mode, the actions are abstracted into the cases of whether the cabin is moving or
   * not.</p>
   *
   * <p>In the case of moving, the current floor will be checked against the current requests, if there is a match,
   * the elevator will arrive at the floor.</p>
   *
   * <p>If the elevator isn't moving, the next request will be determined, and the elevator's state will be updated
   * in accordance with the selected request.</p>
   */
  @Override
  public void run()
  {
    while (true)
    {
      if (cabinMode == CabinMode.EMERGENCY || cabinMode == CabinMode.MAINTENACE)
      {
        // handle special cases
      }
      else normalRun();

      try
      {
        sleep((long)(1 / 60d * 1000));
      } catch (InterruptedException e)
      {
        e.printStackTrace();
      }

//      stillRunning();
//      printRequests();
    }
  }

  /**
   * Get the status of the cabin, i.e. the cabin's current physical state.
   */
  synchronized public CabinStatus getStatus()
  {
    cabinStatus.setFloor(motion.getFloor());
    cabinStatus.setDirection(motion.getDirection());
    cabinStatus.setMode(CabinMode.NORMAL);
    cabinStatus.setCabinRequests(requests);
    cabinStatus.setDestination(motion.getDestination());

    return cabinStatus;
//    return new CabinStatus(motion.getFloor(),
//        motion.getDirection(),
//        CabinMode.NORMAL,
//        new HashSet<>(requests),
//        motion.getDestination());
  }

  /**
   * Set the mode of the cabin.
   *
   * Mode in this sense corresponds to the overall operating mode of the cabin, e.g. normal, emergency.
   */
  synchronized public void updateMode(final CabinMode mode)
  {
    this.cabinMode = mode;
  }

  /**
   * Whether the cabin is currently stationed at a floor.
   */
  synchronized public boolean hasArrived()
  {
    return hasArrived;
  }

  /**
   * Indicate to the cabin that it has arrived at a floor.
   */
  synchronized public void setArrival(final boolean arrived)
  {
    this.hasArrived = arrived;
    if (arrived) motion.setDirection(CabinDirection.STOPPED);
  }

  /**
   * Set the cabins destination, returning if destination is reachable, i.e. the cabin can stop in time.
   */
  public boolean setDestination(final Integer destination)
  {
    if (!hasArrived) moveTowardDestination(motion.getDestination());
    return motion.setDestination(destination);
  }

  /**
   * Clear cabin's requests.
   */
  public void clearRequests()
  {
    // Treat cleared requests as satisfied, i.e. turn their lights off and remove them
    processSatisfiedRequests(requests);
  }

  private void normalRun()
  {
    requests.addAll(cabinRequests.updateRequests());
    if (!hasArrived) movingRun();
  }

  private void movingRun()
  {
    if (motion.getDestination() == null) return;
    if (motion.isAligned() && motion.getFloor() == motion.getDestination())
    {
      final Set<FloorRequest> satisfied = getCurrentlySatisfiedRequests();
      processSatisfiedRequests(satisfied);
      setArrival(true);
      motion.setDestination(null);
    }
  }

  private Set<FloorRequest> getCurrentlySatisfiedRequests()
  {
    return requests.stream()
            .filter(r -> r.getFloor() == motion.getFloor())
            .collect(Collectors.toSet());
  }

  private void processSatisfiedRequests(final Set<FloorRequest> satisfied)
  {
    satisfied.forEach(fr -> cabinRequests.setButtonLight(ButtonLight.OFF, fr.getFloor()));
    requests.removeAll(satisfied);
  }

  public CabinDirection lastDirection;
  private void moveTowardDestination(final Integer destination)
  {
    CabinDirection dir = CabinDirection.STOPPED;
    
    if(destination != null && motion.getFloor() != destination)
    {
      dir = destination - motion.getFloor() > 0 ? CabinDirection.UP : CabinDirection.DOWN;
    }

    if (dir == CabinDirection.STOPPED && motion.getDirection() != CabinDirection.STOPPED) lastDirection = motion.getDirection();
    else if (motion.getDirection() != CabinDirection.STOPPED) lastDirection = motion.getDirection();

    motion.setDirection(dir);
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
      System.out.println("Cabin Running");
    }
    
    if(currentTime - lastCheck >= 5000)
    {
      System.out.println("Cabin Running");
      lastCheck = currentTime;
    }
  }
  
  private void printRequests()
  {
    for(FloorRequest request : requests)
    {
      System.out.println("There is a request for floor "+request.getFloor());
    }
    
    System.out.println();
  }
}