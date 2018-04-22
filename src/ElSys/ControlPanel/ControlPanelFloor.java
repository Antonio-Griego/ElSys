package ElSys.ControlPanel;

import ElSys.Enums.CabinDirection;
import ElSys.FloorRequest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Polygon;

import java.io.IOException;

public class ControlPanelFloor
{
  private ControlPanelFloorView view;
  private ControlPanel controlPanel;
  private final int floorNumber;
  private FloorRequest floorRequest;

  ControlPanelFloor(int floorNum, ControlPanel controlPanel)
  {
    this.floorNumber = floorNum;
    this.controlPanel = controlPanel;
    view = new ControlPanelFloorView(floorNum);
  }

  protected void addRequest(FloorRequest floorRequest)
  {
    if(this.floorRequest != floorRequest)
    {
      Platform.runLater(()-> view.updateArrowLights(floorRequest.getDirection()));
      this.floorRequest = floorRequest;
    }
  }

  protected GridPane getRoot(){return view.floorRoot;}

  private void createRequest(CabinDirection direction)
  {
    if(direction == CabinDirection.UP)
    {
      floorRequest = new FloorRequest(floorNumber, direction);
      controlPanel.getFloorRequests().add(floorRequest);
    }
    else
    {
      floorRequest = new FloorRequest(floorNumber, direction);
      controlPanel.getFloorRequests().add(floorRequest);
    }
  }

  private class ControlPanelFloorView
  {
    GridPane floorRoot;

    @FXML
    Polygon upArrow, downArrow;

    private ControlPanelFloorView(int floorNum)
    {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ControlPanelFloor.fxml"));
      loader.setController(this);

      try
      {
        floorRoot = loader.load();
        ((Label) floorRoot.getChildren().get(0)).setText(floorNum + "");
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }

      upArrow.setOnMouseClicked(event -> arrowPressed(CabinDirection.UP));
      downArrow.setOnMouseClicked(event -> arrowPressed(CabinDirection.DOWN));
    }

    private void arrowPressed(CabinDirection direction)
    {
      updateArrowLights(direction);
      createRequest(direction);
    }

    private void updateArrowLights(CabinDirection direction)
    {
      upArrow.getStyleClass().clear();
      downArrow.getStyleClass().clear();

      switch (direction)
      {
        case UP:
          upArrow.getStyleClass().add("active-arrow");
          downArrow.getStyleClass().add("inactive-arrow");
          break;
        case DOWN:
          upArrow.getStyleClass().add("inactive-arrow");
          downArrow.getStyleClass().add("active-arrow");
          break;
        case STOPPED:
          upArrow.getStyleClass().add("inactive-arrow");
          downArrow.getStyleClass().add("inactive-arrow");
      }
    }


  }
}
