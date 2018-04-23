package ElSys.ControlPanel;

import ElSys.Door;
import ElSys.Enums.CabinDirection;
import ElSys.FloorRequest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
  private Door.DoorState currDoorState = Door.DoorState.CLOSED;

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
      Platform.runLater(()-> view.changeCallLights(floorRequest.getDirection()));
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
                          view.changeCallLights(CabinDirection.STOPPED);
                          view.changeArrivalLights(true);
                        });

    }
    else if (downArrived && callingDown)
    {
      callingDown = false;
      Platform.runLater(()->
                        {
                          view.changeCallLights(CabinDirection.STOPPED);
                          view.changeArrivalLights(false);
                        });
    }
  }

  private void setCallFlags(boolean up, boolean down)
  {
    callingUp = up;
    callingDown = down;
  }

  protected void setDoorState(Door door)
  {
    if(!currDoorState.equals(door.getDoorState()))
    {
      currDoorState = door.getDoorState();
      Platform.runLater(() -> view.changeDoorState(door));

    }
  }

  private class ControlPanelFloorView
  {
    GridPane floorRoot;

    @FXML
    Polygon upArrow, downArrow;

    @FXML
    Button callUp, callDown;

    @FXML
    TextField doorState;

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
          changeCallLights(CabinDirection.UP);
          createRequest(CabinDirection.UP);
        }
        else
        {
          changeCallLights(CabinDirection.DOWN);
          createRequest(CabinDirection.DOWN);
        }
      }
    }

    private void changeDoorState(Door door)
    {
      switch (door.getDoorState())
      {
        case CLOSED:
          doorState.setText("Closed");
          break;
        case OPEN:
          doorState.setText("Open");
          break;
        case OPENING:
          doorState.setText("Opening...");
          break;
        case CLOSING:
          doorState.setText("Closing...");
      }
    }

    private void changeArrivalLights(boolean cabinGoingUp)
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

    private void changeCallLights(CabinDirection direction)
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
