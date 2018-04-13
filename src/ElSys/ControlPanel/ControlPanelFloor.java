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

  public GridPane getRoot(){return view.floorRoot;}

  protected void createRequest(boolean up)
  {
    if(up)
    {
      floorRequest = new FloorRequest(floorNumber, CabinDirection.UP);
      controlPanel.getFloorRequests().add(floorRequest);

    }
    else
    {
      floorRequest = new FloorRequest(floorNumber, CabinDirection.DOWN);
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

      upArrow.setOnMouseClicked(event -> ArrowPressed(true));
      downArrow.setOnMouseClicked(event -> ArrowPressed(false));
    }

    private void ArrowPressed(boolean upRequested)
    {
      createRequest(upRequested);

      if (upRequested)
      {
        Platform.runLater(() -> updateArrowLights(upRequested));
      }
      else
      {
        Platform.runLater(() -> updateArrowLights(upRequested));
      }
    }

    private void updateArrowLights(boolean goingUp)
    {
      upArrow.getStyleClass().clear();
      downArrow.getStyleClass().clear();

      if(goingUp)
      {
        upArrow.getStyleClass().add("active-arrow");
        downArrow.getStyleClass().add("inactive-arrow");
      }
      else
      {
        upArrow.getStyleClass().add("inactive-arrow");
        downArrow.getStyleClass().add("active-arrow");
      }
    }


  }
}
