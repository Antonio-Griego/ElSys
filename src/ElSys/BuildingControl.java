package ElSys;

import ElSys.ControlPanel.ControlPanel;
import ElSys.Enums.BuildingState;
import ElSys.Enums.CabinMode;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.*;

public class BuildingControl extends Thread
{
  private static final int SECONDS_DOORS_OPEN_FOR = 5;

  private ControlPanel controlPanel;
  private Cabin [] cabins;
  private BuildingState buildingState;
  private RequestRouter requestRouter;
  private CabinStatus[] cabinStatuses;
  private Doors[] shafts;
  private Floors floors;
  /**
   * Instantiates the BuildingControl
   * @param cabins
   * @param controlPanel
   */
  public BuildingControl(Cabin[] cabins, ControlPanel controlPanel, Doors[] shafts, Floors floors)
  {
    buildingState = BuildingState.NORMAL;
    this.cabins = cabins;
    this.controlPanel = controlPanel;
    this.requestRouter = new RequestRouter();
    cabinStatuses = new CabinStatus[cabins.length];
    this.shafts = shafts;
    this.floors = floors;
    this.start();
  }

  public void run()
  {
    CabinMode [] cabinModes;
    Set<FloorRequest> floorRequests;
    final ScheduledExecutorService scheduledThreadPoolExecutor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    
    while(true)
    {
      floorRequests = floors.getRequests();
//      floorRequests.addAll(controlPanel.getFloorRequests());
//      buildingState = controlPanel.getBuildingState();
//      cabinModes = controlPanel.getElevatorModes();

      CabinStatus[] cabinStatuses = getStatuses();
      
//      for(int i = 0; i < cabins.length; i++)
//      {
//        cabins[i].updateMode(cabinModes[i]);
//      }
      
      requestRouter.update(cabinStatuses, floorRequests, buildingState);
      Integer[] destinations = requestRouter.getDestinations();
      
      for(int i = 0; i < cabins.length; i++)
      {
        if(shafts[i].areClosed()) cabins[i].setDestination(destinations[i]);
      }
      
      if(buildingState == BuildingState.NORMAL)
      {
        for(int i = 0; i < cabins.length; i++)
        {
          if(cabins[i].hasArrived())
          {
            final Cabin cabin = cabins[i];
            final int idx = i;
            System.out.println("Elevator "+(i+1)+" arrived on floor "+cabins[i].getStatus().getFloor());
            openDoors(cabins[i], i);
            scheduledThreadPoolExecutor.schedule(() -> closeDoors(cabin, idx), SECONDS_DOORS_OPEN_FOR, TimeUnit.SECONDS);
          }
        }
      }
      
      else
      {
        for(int i = 0; i < cabins.length; i++)
        {
          cabins[i].updateMode(CabinMode.EMERGENCY);
        }
      }
      
      controlPanel.update(getStatuses(), buildingState);
      
//      stillRunning();
    }
  }

  private void openDoors(final Cabin cabin, final int cabinIdx)
  {
    final int floorIdx = cabin.getStatus().getFloor();
    System.out.printf("Doors opening for cabin %d on floor %d.\n", cabinIdx, floorIdx);
    shafts[cabinIdx].openDoors(floorIdx);
    floors.setArrivalSignal(floorIdx, cabin.getStatus().getDirection(), true);
  }

  private void closeDoors(final Cabin cabin, final int cabinIdx)
  {
    final int floorIdx = cabin.getStatus().getFloor();
    System.out.printf("Doors closing for cabin %d on floor %d.\n", cabinIdx, floorIdx);
    shafts[cabinIdx].closeDoors(floorIdx);
    while(shafts[cabinIdx].areOpen())
    {
      System.out.printf("Waiting for doors to close for cabin %d on floor %d.\n", cabinIdx, floorIdx);
      try{Thread.sleep(1000);}catch(InterruptedException e){e.printStackTrace();}
    }
    // Cabin won't move when it is set as arrived.
    cabin.setArrival(false);
    floors.resetButton(cabin.getStatus().getFloor(), cabin.getStatus().getDirection());
    floors.setArrivalSignal(floorIdx, cabin.getStatus().getDirection(), false);
  }

  private CabinStatus [] getStatuses()
  {
    for(int i = 0; i < cabins.length; i++)
    {
      cabinStatuses[i] = cabins[i].getStatus();
    }
    
    return cabinStatuses;
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
      System.out.println("BuildingControl Running");
    }
    
    if(currentTime - lastCheck >= 5000)
    {
      System.out.println("BuildingControl Running");
      lastCheck = currentTime;
    }
  }
}