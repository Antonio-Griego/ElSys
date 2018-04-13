package ElSys;

import java.lang.*;

public class FloorAlignment //extends Thread
{
  private SimPhysLocation simPhysLocation;
  private int floor;
//  private worker w;

  FloorAlignment(SimPhysLocation simPhysLocation)
  {
    this.simPhysLocation = simPhysLocation;
//    this.start();
//    w = new worker(simPhysLocation);
  }
  
//  public void run()
//  {
//    Integer currentFloor;
//    while(true)
//    {
//      currentFloor = simPhysLocation.whatFloor();
//      if(currentFloor != null)
//      {
//        floor = currentFloor;
//      }
//    }
//  }
  
  synchronized public Integer getCurrentFloor()
  {
//    return floor;
    if(simPhysLocation.whatFloor() != null)
    {
      floor = simPhysLocation.whatFloor();
    }
    
    return floor;
  }

  synchronized public boolean isAligned()
  {
    return (simPhysLocation.whatFloor() != null);
  }

  synchronized public boolean reachedEndOfShaft()
  {
    return simPhysLocation.reachedEndOfShaft();
  }
}

// worker thread to constantly check if alligned with a floor
// since only this thread sets floor, there should be no concurrency issues.
//class worker extends Thread
//{
//  private SimPhysLocation simPhysLocation;
//  public int floor;
//
//  worker(SimPhysLocation simPhysLocation)
//  {
//    this.simPhysLocation = simPhysLocation;
//    this.start();
//  }
//
//  public void run()
//  {
//    while (true)
//    {
//      work();
//    }
//  }
//
//  public void work()
//  {
//    int location = simPhysLocation.getAlignedFloor();
//    if (location == -1){}
//    else
//    {
//      floor = location;
//    }
//  }
//}