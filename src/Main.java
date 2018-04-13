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
    Cabin [] cabins = new Cabin[CABINS];
  
    SimButton[] buttons = new SimButton[FLOORS];
    SimPhysLocation simPhysLocation;
  
    for(int i = 0; i < CABINS; i++)
    {
      simPhysLocation = new SimPhysLocation(FLOORS);
    
      for(int j = 0; j < FLOORS; j++)
      {
        buttons[j] = new SimButton(j);
      }
    
      cabins[i] = new Cabin(buttons, simPhysLocation);
      cabins[i].start();
    }
  
    CabinStatus[] cabinStatuses = new CabinStatus[cabins.length];
  
    for(int i = 0; i < cabins.length; i++)
    {
      cabinStatuses[i] = cabins[i].getStatus();
    }
    
    ControlPanel controlPanel = new ControlPanel(cabinStatuses, BuildingState.NORMAL);
    buildingControl = new BuildingControl(cabins, controlPanel);

    final SimActions sa = new SimActions(10, Arrays.asList(buttons));
    sa.beginRandomActions();
  }

  public static void main(String[] args)
  {
    launch(args);
  }
}