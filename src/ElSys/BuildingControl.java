package ElSys;

import java.util.ArrayList;

public class BuildingControl
{
  private SimPhysLocation simPhysLocation;
  private ControlPanel controlPanel;
  private ArrayList<Cabin> cabins;
  private final int numberOfFloors;

  BuildingControl(int numberOfFloors, SimPhysLocation simPhysLocation)
  {
    this.simPhysLocation = simPhysLocation;
    this.controlPanel = new ControlPanel();
    this.cabins = new ArrayList();
    this.numberOfFloors = numberOfFloors;

    for(int i = 0; i < numberOfFloors; i++)
    {
      cabins.add(new Cabin(numberOfFloors, simPhysLocation));
    }
  }

  public void run()
  {
    System.out.println("buildingControl.run()");
  }
}