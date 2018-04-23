package ElSys.ControlPanel;

import ElSys.*;
import ElSys.Enums.BuildingState;
import ElSys.Enums.CabinMode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class ControlPanel
{
  private ControlPanelView view;
  private final int totalFloors;
  private final int numCabins;
  private ArrayList<ControlPanelCabin> cabins = new ArrayList<>();
  private ArrayList<ControlPanelFloor> controlFloors = new ArrayList<>();
  private CabinStatus[] cabinStatuses;
  private BuildingState buildingState;
//  private Queue<FloorRequest> floorRequests = new LinkedList<>();
  private Door[] floorDoors;
  private Door[] cabinDoors;

  private Set<FloorRequest> floorRequests = new HashSet<>();


  public ControlPanel(CabinStatus[] cabinStatuses, Door[] floorDoors, Door[] cabinDoors, BuildingState buildingState)
  {
    this.cabinStatuses = cabinStatuses;
    this.buildingState = buildingState;
    this.floorDoors = floorDoors;
    this.cabinDoors = cabinDoors;
    numCabins = cabinStatuses.length;
    //TODO: allow variable floors
    totalFloors = 10;

    addCabins();
    addFloors();
    view = new ControlPanelView(cabins);
  }

  //Used for testing
  public ControlPanel(int totalFloors, int numCabins)
  {
    this.totalFloors = totalFloors;
    this.numCabins = numCabins;
    
    addCabins(numCabins);
    addFloors();
    view = new ControlPanelView(cabins);

  }

  //Used for testing
  private void addCabins(int numCabins)
  {

    for (int i = 0; i < numCabins; i++)
    {
      ControlPanelCabin cabin = new ControlPanelCabin(i + 1);
      cabins.add(cabin);
    }
  }

    private void addCabins()
    {
      int idx = 0;
      for (CabinStatus cabinStatus : cabinStatuses)
      {
        ControlPanelCabin cabin = new ControlPanelCabin(cabinStatus, cabinDoors[idx], idx + 1);
        cabins.add(cabin);
        idx++;
      }
    }

    private void addFloors()
    {
      for (int i = 0; i < totalFloors; i++)
      {
        ControlPanelFloor floor = new ControlPanelFloor(i+1, this);
        controlFloors.add(floor);
      }
    }

  /**
   * @return A Set containing Requests.
   */
  public Set<FloorRequest> getFloorRequests()
  {
    return floorRequests;
  }

  /**
   * @return BuildingState enum that is presently being held by ControlPanel
   */
  public BuildingState getBuildingState()
  {
    return buildingState;
  }

  public CabinMode[] getElevatorModes()
  {
    CabinMode[] modes = new CabinMode[cabins.size()];

    for(int i = 0 ; i < cabins.size(); i++)
    {
      modes[i] = cabins.get(i).getMode();
    }

    return modes;
  }

  //TODO: Must be able to update floor requests from lobby
  public void update(CabinStatus[] cabinStatuses,
                     Floors floors,
                     BuildingState buildingState)
  {
    this.cabinStatuses = cabinStatuses;
    this.buildingState = buildingState;

    updateCabins();
    updateFloors(floors);

  }

  private void updateCabins()
  {
    int idx = 0;

    for(CabinStatus cabinStatus: cabinStatuses)
    {
      cabins.get(idx).update(cabinStatus, cabinDoors[idx]);
      idx++;
    }
  }

  private void updateFloors(Floors floors)
  {
    Set<FloorRequest> newRequests = floors.getRequests();

    if (!newRequests.equals(this.floorRequests))
    {
      for (FloorRequest request : newRequests)
      {
        ControlPanelFloor floor = controlFloors.get(request.getFloor());
        floor.addRequest(request);
      }
      this.floorRequests = newRequests;
    }

    updateArrivals(floors);
    updateFloorDoors();
  }

  private void updateFloorDoors()
  {

    for (int i =0; i<controlFloors.size(); i++)
    {
      controlFloors.get(i).setDoorState(floorDoors[i]);
    }
  }

  private void updateArrivals(Floors floors)
  {
    ArrivalSignal[] up = floors.getUp_Signals();
    ArrivalSignal[] down = floors.getDown_Signals();

    for(int i = 0; i < controlFloors.size(); i++)
    {
      if(i==0)
      {
        controlFloors.get(i).setArrivals(up[i].isLightOn(), false);
      }
      else if (i == 9)
      {
        controlFloors.get(i).setArrivals(false, down[i].isLightOn());
      }
      else
      {
        controlFloors.get(i).setArrivals(up[i].isLightOn(), down[i].isLightOn());
      }
    }
  }

  private class ControlPanelView
  {
    AnchorPane root;

    @FXML
    VBox floors;

    @FXML
    Label floorNumber;

    @FXML
    TabPane cabinTabs;

    private ControlPanelView(ArrayList<ControlPanelCabin> cabins)
    {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ControlPanel.fxml"));
      loader.setController(this);
      Stage stage = new Stage();
      Scene scene;

      try
      {
        root = loader.load();
        scene = new Scene(root);
        stage.setScene(scene);
      } catch (IOException e)
      {
        e.printStackTrace();
      }

      setTabs(cabins);
      addFloorsToRoot(controlFloors);

      stage.show();
    }

    private void setTabs(ArrayList<ControlPanelCabin> cabins)
    {
      for(ControlPanelCabin cabin: cabins)
      {
        cabinTabs.getTabs().add(cabin.getTab());
      }
    }

    private void addFloorsToRoot(ArrayList<ControlPanelFloor> controlFloors)
    {

      for(ControlPanelFloor floor: controlFloors)
      {
        GridPane pane = floor.getRoot();
        floors.getChildren().add(pane);
      }
    }
  }
}