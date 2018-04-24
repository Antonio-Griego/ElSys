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
  private Door.DoorState[] prevDoorStates;

  ControlPanelFloor(int floorNum, ControlPanel controlPanel)
  {
    this.floorNumber = floorNum;
    this.controlPanel = controlPanel;
    prevDoorStates = new Door.DoorState[]{Door.DoorState.CLOSED, Door.DoorState.CLOSED,
                                          Door.DoorState.CLOSED, Door.DoorState.CLOSED};
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

  private void addNewRequest(CabinDirection direction, int floorNum)
  {
    floorRequest = new FloorRequest(floorNumber, direction);

    controlPanel.addFloorRequest(floorRequest);
  }

  protected void setArrivals(boolean upArrived, boolean downArrived)
  {
    if(upArrived && callingUp)
    {
      callingUp = false;
      Platform.runLater(()->
                        {
                          view.changeCallLights(CabinDirection.STOPPED);
                          view.changeArrivalLights(CabinDirection.UP);
                        });

    }
    else if (downArrived && callingDown)
    {
      callingDown = false;
      Platform.runLater(()->
                        {
                          view.changeCallLights(CabinDirection.STOPPED);
                          view.changeArrivalLights(CabinDirection.DOWN);
                        });
    }
  }

  private void setCallFlags(boolean up, boolean down)
  {
    callingUp = up;
    callingDown = down;
  }

  protected void setDoorStates(Door door, int doorIdx)
  {
    Door.DoorState prevState = prevDoorStates[doorIdx];

    if (!prevState.equals(door.getDoorState()))
    {
      if(prevState == Door.DoorState.CLOSING && door.getDoorState() == Door.DoorState.CLOSED)
      {
        Platform.runLater(()-> view.changeArrivalLights(CabinDirection.STOPPED));
      }

      prevDoorStates[doorIdx] = door.getDoorState();
      Platform.runLater(() -> view.changeDoorState(door, doorIdx+1));
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
    TextField doorState1, doorState2, doorState3, doorState4;

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

      callUp.setOnMouseClicked(event -> callButtonPressed(true, floorNumber));
      callDown.setOnMouseClicked(event -> callButtonPressed(false, floorNumber));
    }

    private void callButtonPressed(boolean goingUP, int floorNum)
    {
      //Ignore if a previous request has already been made.
      if(!callingUp && !callingDown)
      {
        if (goingUP)
        {
          Platform.runLater(()->changeCallLights(CabinDirection.UP));
          addNewRequest(CabinDirection.UP, floorNum);
        }
        else
        {
          Platform.runLater(()->changeCallLights(CabinDirection.DOWN));
          addNewRequest(CabinDirection.DOWN, floorNum);
        }
      }
    }

    private TextField getCorrectCabinDoor(int doorNum)
    {
      switch (doorNum)
      {
        case 1: return doorState1;
        case 2: return doorState2;
        case 3: return doorState3;
        case 4: return doorState4;
      }
      return null;
    }

    private void changeDoorState(Door door, int doorNum)
    {
      TextField doorField = getCorrectCabinDoor(doorNum);

      switch (door.getDoorState())
      {
        case CLOSED:
          doorField.setText("Closed");
          break;
        case OPEN:
          doorField.setText("Open");
          break;
        case OPENING:
          doorField.setText("Opening...");
          break;
        case CLOSING:
          doorField.setText("Closing...");
      }
    }

    private void changeArrivalLights(CabinDirection direction)
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
