package ElSys;

import ElSys.Enums.BuildingState;

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
    }
    
  }

  public void run()
  {
    System.out.println("buildingControl.run()");
  }
}