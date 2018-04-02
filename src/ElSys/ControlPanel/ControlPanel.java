package ElSys.ControlPanel;

import ElSys.CabinStatus;
import ElSys.Enums.BuildingState;
import ElSys.Enums.CabinMode;
import ElSys.FloorRequest;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

//Floors will have buttons and door status
//Cabins will have interior buttons,location, direction
public class ControlPanel
{
  private ControlPanelView view;
  private final int totalFloors;
  private final int numCabins;
  private ArrayList<ControlPanelCabin> cabins = new ArrayList<>();
  private ArrayList<ControlPanelFloor> controlFloors = new ArrayList<>();
  private CabinStatus[] cabinStatuses;
  private BuildingState buildingState;
  private Queue<FloorRequest> floorRequests = new LinkedList<>();


  public ControlPanel(CabinStatus[] cabinStatuses, BuildingState buildingState)
  {
    this.cabinStatuses = cabinStatuses;
    this.buildingState = buildingState;
    numCabins = cabinStatuses.length;
    //TODO: allow variable floors
    totalFloors = 4;

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
        ControlPanelCabin cabin = new ControlPanelCabin(cabinStatus, idx + 1);
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
   * @return A Request object containing the request Direction [UP, DOWN] and the
   * floor number of the request if one has been made. Otherwise, returns null.
   */
  public Queue<FloorRequest> getFloorRequests()
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

  public void update(CabinStatus[] cabinStatuses, BuildingState buildingState)
  {
    this.cabinStatuses = cabinStatuses;
    this.buildingState = buildingState;

    updateCabins();
  }

  private void updateCabins()
  {
    int idx = 0;

    for(CabinStatus cabinStatus: cabinStatuses)
    {
      cabins.get(idx).update(cabinStatus);
      idx++;
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
      FXMLLoader loader =  new FXMLLoader(getClass().getResource("/view/ControlPanel.fxml"));
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
      setFloors(controlFloors);

      stage.show();
    }

    private void setTabs(ArrayList<ControlPanelCabin> cabins)
    {
      for(ControlPanelCabin cabin: cabins)
      {
        cabinTabs.getTabs().add(cabin.getTab());
      }
    }

    private void setFloors(ArrayList<ControlPanelFloor> controlFloors)
    {

      for(ControlPanelFloor floor: controlFloors)
      {
        GridPane pane = floor.getRoot();
        floors.getChildren().add(pane);
      }
    }
  }
}