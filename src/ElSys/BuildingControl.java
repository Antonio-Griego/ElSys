package ElSys;

import ElSys.ControlPanel.ControlPanel;
import ElSys.Enums.BuildingState;
import ElSys.Enums.CabinDirection;
import ElSys.Enums.CabinMode;

import java.util.Arrays;
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

  private final ScheduledExecutorService doorCloseExecutor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

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
    while(true)
    {
      updateCabinRequests();
      updateStatuses();
      setDestinations();
      
      if (buildingState == BuildingState.NORMAL) checkForArrivals();
      else setEmergencyModes();
      
      controlPanel.update(cabinStatuses, buildingState);
    }
  }

  private void setEmergencyModes()
  {
    for(int i = 0; i < cabins.length; i++)
    {
      cabins[i].updateMode(CabinMode.EMERGENCY);
    }
  }

  private void checkForArrivals()
  {
    for(int i = 0; i < cabins.length; i++)
    {
      if(cabins[i].hasArrived() && shafts[i].areClosed() && cabinStatuses[i].getDestination() != null)
      {
        final Cabin cabin = cabins[i];
        final int idx = i;
        final CabinDirection directionBeforeStopping = cabin.lastDirection;
        System.out.println("Elevator "+(i+1)+" arrived on floor "+cabins[i].getStatus().getFloor());
        openDoors(cabins[i], i);
        doorCloseExecutor.schedule(() -> closeDoors(cabin, idx, directionBeforeStopping), SECONDS_DOORS_OPEN_FOR, TimeUnit.SECONDS);
      }
    }
  }

  private void setDestinations()
  {
    final Set<FloorRequest> floorRequests = floors.getRequests();
    requestRouter.update(cabinStatuses, floorRequests, buildingState);
    final Integer[] destinations = requestRouter.getDestinations();

    for(int i = 0; i < cabins.length; i++)
    {
      if(shafts[i].areClosed()) cabins[i].setDestination(destinations[i]);
    }
  }

  private void updateCabinRequests()
  {
    Arrays.stream(cabins).forEach(Cabin::updateRequests);
  }

  private void openDoors(final Cabin cabin, final int cabinIdx)
  {
    if (cabin.getStatus().getDestination() == null) return;
    final int floorIdx = cabin.getStatus().getFloor();
    System.out.printf("Doors opening for cabin %d on floor %d.\n", cabinIdx + 1, floorIdx);
    shafts[cabinIdx].openDoors(floorIdx);
    floors.setArrivalSignal(floorIdx, cabin.getStatus().getDirection(), true);
    floors.resetButton(floorIdx, CabinDirection.UP);
    floors.resetButton(floorIdx, CabinDirection.DOWN);
  }

  private void closeDoors(final Cabin cabin, final int cabinIdx, final CabinDirection directionBeforeStopping)
  {
    final int floorIdx = cabin.getStatus().getFloor();
    System.out.printf("Doors closing for cabin %d on floor %d.\n", cabinIdx + 1, floorIdx);
    shafts[cabinIdx].closeDoors(floorIdx);
    while(shafts[cabinIdx].areOpen()) // Wait until doors are closed
    {
      System.out.printf("Waiting for doors to close for cabin %d on floor %d.\n", cabinIdx + 1, floorIdx);
      try{Thread.sleep(1000);}catch(InterruptedException e){e.printStackTrace();}
    }
    // Cabin won't move when it is set as arrived.
    cabin.setArrival(false);
    floors.setArrivalSignal(floorIdx, directionBeforeStopping, false);
  }

  private void updateStatuses()
  {
    for(int i = 0; i < cabins.length; i++)
    {
      cabinStatuses[i] = cabins[i].getStatus();
    }
  }

}