import ElSys.*;
import ElSys.ControlPanel.ControlPanel;
import ElSys.Enums.BuildingState;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Random;

public class Main extends Application
{
  private final int FLOORS = 10;
  private final int CABINS = 1;
  private BuildingControl buildingControl;
  @Override
  public void start(Stage primaryStage) throws Exception
  {
    Doors[] shafts = new Doors[CABINS];
    Cabin [] cabins = new Cabin[CABINS];
  
    SimButton[] buttons = new SimButton[FLOORS];
    SimPhysLocation simPhysLocation;
    Door cabinDoor;
    Door[] floorDoors;
  
    for(int i = 0; i < CABINS; i++)
    {
      simPhysLocation = new SimPhysLocation(FLOORS);
      cabinDoor = new Door(new SimDoor());
      floorDoors = new Door[FLOORS];
    
      for(int j = 0; j < FLOORS; j++)
      {
        buttons[j] = new SimButton(j);
        floorDoors[i] = new Door(new SimDoor());
      }
    
      shafts[i] = new Doors(floorDoors, cabinDoor);
      cabins[i] = new Cabin(buttons, simPhysLocation);
      cabins[i].start();
    }
  
    CabinStatus[] cabinStatuses = new CabinStatus[cabins.length];
  
    for(int i = 0; i < cabins.length; i++)
    {
      cabinStatuses[i] = cabins[i].getStatus();
    }
    
    
    
    ControlPanel controlPanel = new ControlPanel(cabinStatuses, BuildingState.NORMAL);
    buildingControl = new BuildingControl(cabins, controlPanel, shafts);

    final SimActions sa = new SimActions(10, Arrays.asList(buttons));
    sa.beginRandomActions();
  }

  public static void main(String[] args)
  {
    launch(args);
  }
}