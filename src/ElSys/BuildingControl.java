package ElSys;

import java.util.ArrayList;

public class BuildingControl extends Thread
{
  private ControlPanel controlPanel;
  private Cabin [] cabins;

  BuildingControl(int numberOfFloors, int numElevators)
  {
    cabins = new Cabin[numElevators];
    
    for(int i = 0; i < numElevators)
    {
    
    }
    
  }

  public void run()
  {
    System.out.println("buildingControl.run()");
  }
}