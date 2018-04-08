package ElSys;

import java.lang.*;

public class FloorAlignment
{
  private SimPhysLocation simPhysLocation;
  private int floor;
  private worker w;

  FloorAlignment(SimPhysLocation simPhysLocation)
  {
    this.simPhysLocation = simPhysLocation;
    w = new worker(simPhysLocation);
  }

  // when called, grabs the most current floor from worker thread and returns it
  public int getCurrentFloor()
  {
    floor = w.floor;
    return floor;
  }

  public boolean isAligned()
  {
    if (simPhysLocation.getAlignedFloor() == -1){return false;}
    else {return true;}
  }

  public boolean reachedEndOfShaft()
  {
    return simPhysLocation.reachedEndOfShaft();
  }
}

// worker thread to constantly check if alligned with a floor
// since only this thread sets floor, there should be no concurrency issues.
class worker extends Thread
{
  private SimPhysLocation simPhysLocation;
  public int floor;

  worker(SimPhysLocation simPhysLocation)
  {
    this.simPhysLocation = simPhysLocation;
    start();
  }

  public void run()
  {
    while (true)
    {
      work();
    }
  }

  public void work()
  {
    int location = simPhysLocation.getAlignedFloor();
    if (location == -1){}
    else
    {
      floor = location;
    }
  }
}