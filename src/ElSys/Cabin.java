package ElSys;

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
public class Cabin //extends Thread
{

  private CabinStatus cabinStatus;
  private CabinRequests cabinRequests;
  private Motion motion;
  private final int numberOfFloors;

  private final Queue<FloorRequest> requests = new LinkedList<>();

  /**
   * Create a new cabin with {@code numberOfFloors} floors using the {@link SimPhysLocation} specified.
   *
   * @param numberOfFloors Number of floors that the cabin will service.
   * @param simPhysLocation {@link SimPhysLocation} that will be used.
   */
  public Cabin(final int numberOfFloors, final SimPhysLocation simPhysLocation)
  {
    this.cabinStatus = new CabinStatus();
    this.numberOfFloors = numberOfFloors;
    this.cabinRequests = new CabinRequests(numberOfFloors);
    this.motion = new Motion(simPhysLocation);
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
    if (cabinStatus.getMode() == CabinMode.EMERGENCY) motion.setDirection(CabinDirection.NOT_MOVING);
    else normalRun();
  }

  /**
   * Add request to the queue to be considered once the elevator has stopped.
   * @param floorRequest Request to be later considered.
   */
  public void addRequest(final FloorRequest floorRequest)
  {
    requests.add(floorRequest);
  }

  /**
   * Get the status of the cabin, i.e. the cabin's current physical state.
   */
  public CabinStatus getStatus()
  {
    return cabinStatus;
  }

  /**
   * Set the mode of the cabin.
   *
   * Mode in this sense corresponds to the overall operating mode of the cabin, e.g. normal, emergency.
   */
  public void updateMode(final CabinMode mode)
  {
    cabinStatus.setMode(mode);
  }

  /**
   * Whether the cabin is currently stationed at a floor.
   */
  public boolean hasArrived()
  {
    return motion.getDirection() == CabinDirection.NOT_MOVING;
  }

  /**
   * Indicate to the cabin that it has arrived at a floor.
   */
  public void setArrival(final boolean hasArrived)
  {
    if (hasArrived) stopCabin();
  }

  /**
   * Get the number of requests that the cabin is currently processing.
   */
  public int getNumRequests()
  {
    return requests.size();
  }

  private void normalRun()
  {
    requests.addAll(cabinRequests.updateRequests());
    if (motion.getDirection() == CabinDirection.NOT_MOVING) notMovingRun();
    else movingRun();
  }

  private void movingRun()
  {
    final Set<FloorRequest> atFloorRequests = requests.stream()
            .filter(fr -> fr.getFloor() == motion.getFloor())
            .collect(Collectors.toSet());

    processSatisfiedRequests(atFloorRequests);
  }

  private void processSatisfiedRequests(Set<FloorRequest> atFloorRequests)
  {
    if (atFloorRequests.isEmpty()) return;
    requests.removeAll(atFloorRequests);
    atFloorRequests.forEach(cabinRequests::satisfyRequest);
    setArrival(true);
  }

  private void notMovingRun()
  {
    final FloorRequest nextRequest = getNextRequest();
    if (nextRequest == null) return;
    moveTowardRequest(nextRequest);
  }

  private void moveTowardRequest(final FloorRequest nextRequest)
  {
    final CabinDirection dir =
            nextRequest.getDirection() != null ? nextRequest.getDirection() :
            nextRequest.getFloor() - motion.getFloor() > 0 ? CabinDirection.UP :
                    CabinDirection.DOWN;

    motion.setDirection(dir);
    cabinStatus.setDirection(dir);
  }

  private FloorRequest getNextRequest()
  {
    return requests.stream()
            .min(Comparator.comparingInt(r -> Math.abs(motion.getFloor() - r.getFloor())))
            .orElse(null);
  }

  private void stopCabin()
  {
    motion.stop();
    cabinStatus.setDirection(CabinDirection.NOT_MOVING);
  }
}