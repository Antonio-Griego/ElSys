package ElSys.ControlPanel;

import ElSys.CabinStatus;
import ElSys.Door;
import ElSys.Enums.ButtonLight;
import ElSys.Enums.CabinDirection;
import ElSys.Enums.CabinMode;
import ElSys.FloorRequest;
import ElSys.SimButton;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polygon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ControlPanelCabin
{
  private IntegerProperty currFloor = new SimpleIntegerProperty(0);
  private int cabinNumber;
  private ControlPanel controlPanel;
  private CabinMode mode;
  private CabinDirection currDirection = CabinDirection.STOPPED;
  private ControlPanelCabinView view;
  private Set<FloorRequest> currFloorRequests = new HashSet<>();
  private Set<FloorRequest> newFloorRequests = new HashSet<>();
  private Door.DoorState currDoorState = Door.DoorState.CLOSED;
  private SimButton[] cabinButtons;
  private boolean[] buttonsActivated;



  //TODO: For testing. Remove when complete
  ControlPanelCabin(int cabinNumber)
  {
    view = new ControlPanelCabinView(cabinNumber, this);
  }

  //TODO allow for variable floor #'s. Loaded dynamically
  ControlPanelCabin(ControlPanel panel,
                    CabinStatus cabinStatus,
                    Door cabinDoor,
                    int cabinNumber,
                    SimButton[] cabinButtons)
  {
    view = new ControlPanelCabinView(cabinNumber, this);
    this.cabinButtons = cabinButtons;
    this.controlPanel = panel;
    this.cabinNumber = cabinNumber;
    buttonsActivated = new boolean[cabinButtons.length];
    update(cabinStatus, cabinDoor);
    currFloor.addListener((obs, oldVal, newVal) -> view.updateFloorLight((int)oldVal,(int)newVal));
  }

  private void addNewRequest(int floor)
  {
//    boolean pressed = checkIfPressed(floor);
//    cabinButtons[floor].setLight(true);
    Platform.runLater(() -> view.updateButtonLight(floor, true));

    controlPanel.addCabinRequest(new FloorRequest(floor, null), cabinNumber-1);

//    newFloorRequests.add();
  }

  protected Tab getTab()
  {
    return view.tab;
  }

  protected CabinMode getMode()
  {
    return mode;
  }

  protected Set<FloorRequest> getNewFloorRequests() {return newFloorRequests;}

  protected int getCurrentFloor(){return currFloor.get();}

  protected void setCurrentFloor(int floor){currFloor.set(floor);}

  protected boolean checkIfPressed(int floor)
  {
    return currFloorRequests
                    .stream()
                    .anyMatch(request -> request.getFloor() == floor);
  }

  protected void update(CabinStatus cabinStatus, Door door)
  {
    if(currFloor.get() != cabinStatus.getFloor())
    {
      updateFloor(cabinStatus.getFloor());
    }
    if(currDirection != cabinStatus.getDirection())
    {
      updateDirection(cabinStatus.getDirection());
    }
    if(!currDoorState.equals(door.getDoorState()))
    {
      currDoorState = door.getDoorState();
      Platform.runLater(() -> view.changeDoorState(door));
    }

//    updateCabinButtons();

    //We shouldn't be updating the mode from anywhere except the GUI.
    //Only maintenance should update the mode.
//    if(mode != cabinStatus.getMode())
//    {
//      updateMode(cabinStatus.getMode());
//    }

    updateCabinRequests(cabinStatus.getCabinRequests());
  }

  private void updateCabinButtons()
  {
    for(int i = 0; i< buttonsActivated.length; i++)
    {
      boolean buttonOn = buttonsActivated[i];
      SimButton cabinButton = cabinButtons[i];
      int floor = i;

      if(buttonOn && (!cabinButton.getLight()))
      {
        Platform.runLater(()-> view.updateButtonLight(floor, false));
      }
      else if(!buttonOn && (cabinButton.getLight()))
      {
        Platform.runLater(()-> view.updateButtonLight(floor, true));
      }
    }
  }

  private void updateCabinRequests(Set<FloorRequest> floorRequests)
  {
    if(!currFloorRequests.equals(floorRequests))
    {
      currFloorRequests = floorRequests;
      Platform.runLater(() -> view.updateFloorRequests(currFloorRequests));
    }
  }

  private void updateDirection(CabinDirection newDirection)
  {
    if (currDirection != newDirection)
    {
      currDirection = newDirection;
      Platform.runLater(()->view.updateDirectionLight(currDirection));
    }
  }

  private void updateFloor(int floor)
  {
    currFloor.set(floor);
  }

  private void updateMode(CabinMode mode)
  {
    if (this.mode != mode)
    {
      this.mode = mode;
      Platform.runLater(() -> view.updateModeGroup(mode));
    }
  }

  private void userUpdatesMode(String mode)
  {
    switch (mode)
    {
      case "Normal":
        this.mode = CabinMode.NORMAL;
        break;
      case "Maintenance":
        this.mode = CabinMode.MAINTENACE;
        break;
      case "Emergency":
        this.mode = CabinMode.EMERGENCY;
    }
  }

  private class ControlPanelCabinView
  {
    ControlPanelCabin controller;
    private ArrayList<javafx.scene.control.Button> floors = new ArrayList<>();
    private ArrayList<javafx.scene.control.Button> cabinButtons = new ArrayList<>();
    private Tab tab;

    @FXML
    Button buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive, buttonSix,
            buttonSeven, buttonEight, buttonNine, buttonTen,
            floorOne, floorTwo, floorThree, floorFour, floorFive, floorSix, floorSeven,
            floorEight, floorNine, floorTen;

    @FXML
    Polygon upArrow, downArrow;

    @FXML
    RadioButton normalMode, maintenanceMode, emergencyMode;

    @FXML
    TextField doorState;

    private ControlPanelCabinView(int cabinNumber, ControlPanelCabin controller)
    {
      this.controller = controller;

      FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ControlPanelCabin.fxml"));
      loader.setController(this);
      tab = new Tab();
      tab.setText("Cabin " + cabinNumber);

      try
      {

        AnchorPane pane = loader.load();
        tab.setContent(pane);
      } catch (IOException e)
      {
        e.printStackTrace();
      }

      addFloors();
      addButtons();
    }

    private void addFloors()
    {
      floors.add(floorOne);
      floors.add(floorTwo);
      floors.add(floorThree);
      floors.add(floorFour);
      floors.add(floorFive);
      floors.add(floorSix);
      floors.add(floorSeven);
      floors.add(floorEight);
      floors.add(floorNine);
      floors.add(floorTen);
    }

    private void addButtons()
    {
      cabinButtons.add(buttonOne);
      cabinButtons.add(buttonTwo);
      cabinButtons.add(buttonThree);
      cabinButtons.add(buttonFour);
      cabinButtons.add(buttonFive);
      cabinButtons.add(buttonSix);
      cabinButtons.add(buttonSeven);
      cabinButtons.add(buttonEight);
      cabinButtons.add(buttonNine);
      cabinButtons.add(buttonTen);

      cabinButtons.forEach(button -> button.setOnAction(this::cabinRequestPressed));

      normalMode.setOnAction(this::modeChanged);
      emergencyMode.setOnAction(this::modeChanged);
      maintenanceMode.setOnAction(this::modeChanged);
    }

    private void cabinRequestPressed(ActionEvent event)
    {
      Button button = ((Button) event.getSource());
      addNewRequest(cabinButtons.indexOf(button));
    }

    private void modeChanged(ActionEvent event)
    {
      RadioButton button = ((RadioButton) event.getSource());
      userUpdatesMode(button.getText());
    }

    private void updateButtonLight(int floor, boolean turnOn)
    {
      Button button = cabinButtons.get(floor);
      updateButtonLight(button, turnOn);
    }

    private void updateButtonLight(Button button, boolean turnOn)
    {
      button.getStyleClass().clear();

      if (turnOn)
      {
        button.getStyleClass().add("active-cabin-button");
      }
      else
      {
        button.getStyleClass().add("inactive-cabin-button");
      }
    }

    private void updateDirectionLight(CabinDirection newDirection)
    {
      upArrow.getStyleClass().clear();
      downArrow.getStyleClass().clear();

      switch (newDirection)
      {
        case STOPPED:
          upArrow.getStyleClass().add("inactive-arrow");
          downArrow.getStyleClass().add("inactive-arrow");
          break;

        case UP:
          upArrow.getStyleClass().add("active-arrow");
          downArrow.getStyleClass().add("inactive-arrow");
          break;

        case DOWN:
          upArrow.getStyleClass().add("inactive-arrow");
          downArrow.getStyleClass().add("active-arrow");
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

    private void updateFloorLight(int prevFloor, int currentFloor)
    {
      Button prev = floors.get(prevFloor);
      Button curr = floors.get(currentFloor);

      Platform.runLater(() ->
                        {
                          prev.getStyleClass().clear();
                          curr.getStyleClass().clear();
                          prev.getStyleClass().add("inactive-floor");
                          curr.getStyleClass().add("active-floor");
                        });
    }

    private void updateModeGroup(CabinMode mode)
    {
      switch (mode)
      {
        case NORMAL:
          normalMode.fire();
          break;
        case MAINTENACE:
          maintenanceMode.fire();
          break;
        case EMERGENCY:
          emergencyMode.fire();
      }
    }

    //TODO: update to only turn on/off buttons which are needed.
    // Currently turning all off then turning appropriate ones on.
    private void updateFloorRequests(Set<FloorRequest> floorRequests)
    {
      cabinButtons.forEach(button -> view.updateButtonLight(button, false));

      for (FloorRequest request : floorRequests)
      {
        Button button = cabinButtons.get(request.getFloor()-1);
        view.updateButtonLight(button, true);
      }
    }
  }
}



