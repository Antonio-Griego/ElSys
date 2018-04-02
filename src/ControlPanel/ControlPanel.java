package ControlPanel;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

//Floors will have buttons and door status

//Cabins will have interior buttons,location, direction
public class ControlPanel
{
  private ControlPanelView view;
  private int totalFloors;
  private ArrayList<ControlPanelCabin> cabins = new ArrayList<>();
  private ArrayList<ControlPanelFloor> floors = new ArrayList<>();

  public ControlPanel(int totalFloors, int numCabins)
  {
    this.totalFloors = totalFloors;

    addCabins(numCabins);
    view = new ControlPanelView(cabins);
  }

  private void addCabins(int numCabins)
  {
    for(int i = 0 ; i < numCabins; i++)
    {
      ControlPanelCabin cabin = new ControlPanelCabin(i+1);
      cabins.add(cabin);
    }
  }

  public void getFloorRequest()
  {

  }

  public void getBuildingState()
  {

  }

  public void getElevatorModes()
  {

  }

  public void update()
  {

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

      addTabs(cabins);
      addFloors(10);

      stage.show();
    }

    private void addTabs(ArrayList<ControlPanelCabin> cabins)
    {
      for(ControlPanelCabin cabin: cabins)
      {
        cabinTabs.getTabs().add(cabin.getTab());
      }
    }


    private void addFloors(int numFloors)
    {

      for(int i = 0 ; i < numFloors; i++)
      {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ControlPanelFloor.fxml"));

        try
        {
          GridPane pane = loader.load();
          ((Label)pane.getChildren().get(0)).setText(i+1+"");
          floors.getChildren().add(pane);
        } catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }

    private void floorButtonPressed()
    {

    }

    private void cabinButtonPressed()
    {

    }

  }
}