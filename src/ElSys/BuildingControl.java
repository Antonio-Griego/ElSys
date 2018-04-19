package ElSys;

import ElSys.ControlPanel.ControlPanel;
import ElSys.Enums.BuildingState;
import ElSys.Enums.CabinMode;

import java.util.Queue;
import java.util.Set;

public class BuildingControl extends Thread
{
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
        cabins[i].setDestination(destinations[i]);
      }
      
      if(buildingState == BuildingState.NORMAL)
      {
        for(int i = 0; i < cabins.length; i++)
        {
          if(cabins[i].hasArrived())
          {
            System.out.println("Elevator "+(i+1)+" arrived on floor "+cabins[i].getStatus().getFloor());
            System.out.println("Doors Opening");
            System.out.println("Doors Closing");
            cabins[i].setArrival(false);
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