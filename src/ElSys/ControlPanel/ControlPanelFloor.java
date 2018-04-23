package ElSys.ControlPanel;

import ElSys.Enums.CabinDirection;
import ElSys.FloorRequest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
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
  private boolean callingUp;
  private boolean callingDown;

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
      Platform.runLater(()-> view.updateCallLights(floorRequest.getDirection()));
      this.floorRequest = floorRequest;
    }
  }

  protected GridPane getRoot(){return view.floorRoot;}

  private void createRequest(CabinDirection direction)
  {
    floorRequest = new FloorRequest(floorNumber, direction);
    controlPanel.getFloorRequests().add(floorRequest);
  }

  protected void setArrivals(boolean upArrived, boolean downArrived)
  {
    if(upArrived && callingUp)
    {
      callingUp = false;
      Platform.runLater(()->
                        {
                          view.updateCallLights(CabinDirection.STOPPED);
                          view.updateArrivalSignal(true);
                        });

    }
    else if (downArrived && callingDown)
    {
      callingDown = false;
      Platform.runLater(()->
                        {
                          view.updateCallLights(CabinDirection.STOPPED);
                          view.updateArrivalSignal(false);
                        });
    }
  }

  private void setCallFlags(boolean up, boolean down)
  {
    callingUp = up;
    callingDown = down;
  }

  private class ControlPanelFloorView
  {
    GridPane floorRoot;

    @FXML
    Polygon upArrow, downArrow;

    @FXML
    Button callUp, callDown;

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

      callUp.setOnMouseClicked(event -> callButtonPressed(true));
      callDown.setOnMouseClicked(event -> callButtonPressed(false));
    }

    private void callButtonPressed(boolean goingUP)
    {
      //Ignore if a previous request has already been made.
      if(!callingUp && !callingDown)
      {
        if (goingUP)
        {
          updateCallLights(CabinDirection.UP);
          createRequest(CabinDirection.UP);
        }
        else
        {
          updateCallLights(CabinDirection.DOWN);
          createRequest(CabinDirection.DOWN);
        }
      }
    }

    private void updateArrivalSignal(boolean cabinGoingUp)
    {
      if(cabinGoingUp)
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

    private void updateCallLights(CabinDirection direction)
    {
      callUp.getStyleClass().clear();
      callDown.getStyleClass().clear();

      switch (direction)
      {
        case UP:
          callUp.getStyleClass().add("active-call-button");
          callDown.getStyleClass().add("inactive-call-button");
          setCallFlags(true, false);
          break;
        case DOWN:
          callUp.getStyleClass().add("inactive-call-button");
          callDown.getStyleClass().add("active-call-button");
          setCallFlags(false, true);
          break;
        case STOPPED:
          callUp.getStyleClass().add("inactive-call-button");
          callDown.getStyleClass().add("inactive-call-button");
          setCallFlags(false, false);
      }
    }


  }
}
