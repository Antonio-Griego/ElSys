package ElSys;

import ElSys.Enums.ButtonLight;
import ElSys.Enums.CabinDirection;
import ElSys.Enums.CabinMode;

import java.util.*;
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
  //@Override
  public void run()
  {
    while (true)
    {
      if(!hasArrived)
      {
        if (cabinMode == CabinMode.EMERGENCY || cabinMode == CabinMode.MAINTENACE)
        {
          motion.setDirection(CabinDirection.STOPPED);
        }
        else normalRun();
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
    return new CabinStatus(motion.getFloor(), motion.getDirection(), cabinMode, requests, motion.getDestination());
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
    return motion.getDestination() == motion.getFloor() && motion.isAligned();
  }

  /**
   * Indicate to the cabin that it has arrived at a floor.
   */
  synchronized public void setArrival(final boolean arrived)
  {
    this.hasArrived = arrived;
  }

  /**
   * Set the cabins destination, returning if destination is reachable, i.e. the cabin can stop in time.
   */
  public boolean setDestination(final Integer destination)
  {
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
    if (motion.getDirection() == CabinDirection.STOPPED) notMovingRun();
    else movingRun();
  }

  private void movingRun()
  {
    if (motion.isAligned() && motion.getFloor() == motion.getDestination())
    {
      motion.stopElevator();

      final Set<FloorRequest> satisfied = getCurrentlySatisfiedRequests();
      processSatisfiedRequests(satisfied);

      motion.setDestination(null);
    }
  }

  private Set<FloorRequest> getCurrentlySatisfiedRequests()
  {
    return requests.stream()
            .filter(r -> r.getFloor() == motion.getDestination())
            .collect(Collectors.toSet());
  }

  private void processSatisfiedRequests(final Set<FloorRequest> satisfied)
  {
    satisfied.forEach(fr -> cabinRequests.setButtonLight(ButtonLight.OFF, fr.getFloor()));
    requests.removeAll(satisfied);
  }

  private void notMovingRun()
  {
    // Not moving, so just start going towards next destination, door code will probably go here.
    moveTowardDestination(motion.getDestination());
  }

  private void moveTowardDestination(final int destination)
  {
    final CabinDirection dir = destination - motion.getFloor() > 0 ?
            CabinDirection.UP :
            CabinDirection.DOWN;

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