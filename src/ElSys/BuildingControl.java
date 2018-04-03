package ElSys;

import ElSys.ControlPanel.ControlPanel;
import ElSys.Enums.BuildingState;
import ElSys.Enums.CabinMode;

import java.util.Random;

public class BuildingControl extends Thread
{
  private ControlPanel controlPanel;
  private Cabin [] cabins;
  private BuildingState buildingState;

  public BuildingControl(int numberOfFloors, int numElevators)
  {
    buildingState = BuildingState.NORMAL;
    cabins = new Cabin[numElevators];
    
    SimButton [] buttons = new SimButton[numberOfFloors];
    SimPhysLocation simPhysLocation;
    Random rand = new Random();
    
    for(int i = 0; i < numElevators; i++)
    {
      simPhysLocation = new SimPhysLocation(numberOfFloors);
      
      for(int j = 0; j < numberOfFloors; j++)
      {
        buttons[j] = new SimButton(rand);
      }
      
      cabins[i] = new Cabin(buttons, simPhysLocation);
      cabins[i].start();
    }
    
    CabinStatus [] cabinStatuses = getStatuses();
    
    controlPanel = new ControlPanel(cabinStatuses, buildingState);
    
    this.start();
  }

  public void run()
  {
    CabinStatus[] cabinStatuses;
    CabinMode [] cabinModes;
    
    while(true)
    {
      controlPanel.getFloorRequests();
      buildingState = controlPanel.getBuildingState();
      cabinStatuses = getStatuses();
      cabinModes = controlPanel.getElevatorModes();

      
      for(int i = 0; i < cabins.length; i++)
      {
        cabins[i].updateMode(cabinModes[i]);
      }

      
      if(buildingState == BuildingState.NORMAL)
      {
        for(int i = 0; i < cabins.length; i++)
        {
          if(cabins[i].hasArrived())
          {
            System.out.println("Elevator "+(i+1)+" arrived on floor "+cabins[i].getStatus().getFloor());
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

      controlPanel.update(cabinStatuses, buildingState);
    }
  }
  
  
  private CabinStatus [] getStatuses()
  {
    CabinStatus[] cabinStatuses = new CabinStatus[cabins.length];
  
    for(int i = 0; i < cabins.length; i++)
    {
      cabinStatuses[i] = cabins[i].getStatus();
    }
    
    return cabinStatuses;
  }
}