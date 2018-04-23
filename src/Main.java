import ElSys.*;
import ElSys.ControlPanel.ControlPanel;
import ElSys.Enums.BuildingState;
import ElSys.Enums.CabinDirection;
import ElSys.ArrivalSignal.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Random;

public class Main extends Application
{
  private final int FLOORS = 10;
  private final int CABINS = 4;
  private BuildingControl buildingControl;
  @Override
  public void start(Stage primaryStage) throws Exception
  {
    Doors[] shafts = new Doors[CABINS];
    Cabin [] cabins = new Cabin[CABINS];
  
    SimButton[] buttons = new SimButton[FLOORS];
    SimPhysLocation simPhysLocation;
    Door[] cabinDoors = new Door[CABINS];
    Door[] floorDoors;
    Door[][] floorCabinDoors = new Door[CABINS][FLOORS];
  
    for(int i = 0; i < CABINS; i++)
    {
      simPhysLocation = new SimPhysLocation(FLOORS, "Cabin "+(i+1));
      cabinDoors[i] = new Door(new SimDoor());
      floorDoors = new Door[FLOORS];

      for(int j = 0; j < FLOORS; j++)
      {
        buttons[j] = new SimButton("Floor "+j+" button in Cabin "+i);
        floorDoors[j] = new Door(new SimDoor());
      }
      floorCabinDoors[i] = floorDoors;
      shafts[i] = new Doors(floorDoors, cabinDoors[i]);
      cabins[i] = new Cabin(buttons, simPhysLocation);
      cabins[i].start();
    }
  
    CabinStatus[] cabinStatuses = new CabinStatus[cabins.length];
  
    for(int i = 0; i < cabins.length; i++)
    {
      cabinStatuses[i] = cabins[i].getStatus();
    }
  
    ArrivalSignal[] up_Sigs = new ArrivalSignal[FLOORS];
    ArrivalSignal[] down_Sigs = new ArrivalSignal[FLOORS];
    Button [] up_Buttons = new Button[FLOORS];
    Button [] down_Buttons = new Button[FLOORS];
  
  
    for(int i = 0; i < FLOORS-1; i++)
    {
      up_Sigs[i] = new ArrivalSignal(new SimSignal(CabinDirection.UP, i));
      up_Buttons[i] = new Button(new SimButton("Floor "+i+" UP button"));
    }

    for(int i = 1; i < FLOORS; i++)
    {
      down_Sigs[i] = new ArrivalSignal(new SimSignal(CabinDirection.DOWN, i));
      down_Buttons[i] = new Button(new SimButton("Floor "+i+" DOWN button"));
    }

    Floors floors = new Floors(up_Buttons, down_Buttons, up_Sigs, down_Sigs);
    
    ControlPanel controlPanel = new ControlPanel(cabinStatuses, floorCabinDoors, cabinDoors, BuildingState.NORMAL);
    buildingControl = new BuildingControl(cabins, controlPanel, shafts, floors);

    final SimActions sa = new SimActions(10, Arrays.asList(buttons), Arrays.asList(up_Buttons), Arrays.asList(down_Buttons));
    sa.beginRandomActions();
  }

  public static void main(String[] args)
  {
    launch(args);
  }
}