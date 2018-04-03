package ElSys;

import ElSys.Enums.CabinDirection;
import ElSys.Enums.CabinMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class CabinStatus
{
  private final int currentFloor;
  private final CabinDirection cabinDirection;
  private final CabinMode cabinMode;
  private final Queue<FloorRequest> cabinRequests;

  CabinStatus(final int currentFloor, final CabinDirection cabinDirection,
              final CabinMode cabinMode, Queue<FloorRequest> cabinRequests)
  {
    this.currentFloor = currentFloor;
    this.cabinDirection = cabinDirection;
    this.cabinMode = cabinMode;
    this.cabinRequests = cabinRequests;
  }

  public int getFloor()
  {
    return currentFloor;
  }

  public CabinDirection getDirection()
  {
    return cabinDirection;
  }

  public CabinMode getMode()
  {
    return cabinMode;
  }

  public Queue<FloorRequest> getCabinRequests(){ return cabinRequests;}
}