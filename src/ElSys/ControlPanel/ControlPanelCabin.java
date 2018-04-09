package ElSys.ControlPanel;

import ElSys.CabinStatus;
import ElSys.Enums.CabinDirection;
import ElSys.Enums.CabinMode;
import ElSys.FloorRequest;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polygon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class ControlPanelCabin
{
  private int currentFloor = 0;
  private CabinMode mode;
  private CabinDirection currDirection = CabinDirection.STOPPED;
  private ControlPanelCabinView view;
  private Set<FloorRequest> floorRequests;



  //TODO: For testing. Remove when complete
  ControlPanelCabin(int cabinNumber)
  {
    view = new ControlPanelCabinView(cabinNumber, this);
  }

  //TODO allow for variable floor #'s. Loaded dynamically
  ControlPanelCabin(CabinStatus cabinStatus, int cabinNumber)
  {
    view = new ControlPanelCabinView(cabinNumber, this);
    update(cabinStatus);
  }

  protected Tab getTab()
  {
    return view.tab;
  }

  protected CabinMode getMode()
  {
    return mode;
  }

  private void modeChanged(ActionEvent event)
  {
    RadioButton button = ((RadioButton) event.getSource());
    switch (button.getText())
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

  protected boolean addFloorRequest(int floor)
  {
    //TODO: verify null value is correct.
    //Find out where direction is being set.
    return floorRequests.add(new FloorRequest(floor, null));
  }

  protected void update(CabinStatus cabinStatus)
  {
    updateFloors(cabinStatus.getFloor());
    updateDirection(cabinStatus.getDirection());
    updateMode(cabinStatus.getMode());
    updateCabinRequests(cabinStatus.getCabinRequests());

  }

  private void updateCabinRequests(Set<FloorRequest> floorRequests)
  {
    this.floorRequests = floorRequests;
    Platform.runLater(()-> view.updateFloorRequests(floorRequests));
  }

  private void updateDirection(CabinDirection newDirection)
  {
    if (currDirection != newDirection)
    {
      currDirection = newDirection;
      Platform.runLater(()->view.updateDirectionLight(currDirection));
    }
  }

  private void updateFloors(int floor)
  {
    if (floor != currentFloor)
    {
      Platform.runLater(() -> view.updateFloorLight(currentFloor, floor));
      currentFloor = floor;
    }
  }

  private void updateMode(CabinMode mode)
  {
    if (this.mode != mode)
    {
      this.mode = mode;
      Platform.runLater(() -> view.updateModeGroup(mode));
    }
  }

  private class ControlPanelCabinView
  {
    ControlPanelCabin controller;
    private ArrayList<Button> floors = new ArrayList<>();
    private ArrayList<Button> cabinButtons = new ArrayList<>();
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

      normalMode.setOnAction(controller::modeChanged);
      emergencyMode.setOnAction(controller::modeChanged);
      maintenanceMode.setOnAction(controller::modeChanged);
    }

    private void cabinRequestPressed(ActionEvent event)
    {
      Button button = ((Button) event.getSource());
      boolean newRequest = addFloorRequest(cabinButtons.indexOf(button));
      
      if(newRequest)
      {
        updateButtonLight(button, true);
      }
    }

    private void updateButtonLight(Button button, boolean turnOn)
    {
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

    private void updateFloorLight(int prevFloor, int currentFloor)
    {
      Button prev = floors.get(prevFloor);
      Button curr = floors.get(currentFloor);

      prev.getStyleClass().add("inactive-floor");
      curr.getStyleClass().add("active-floor");
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
        Button button = cabinButtons.get(request.getFloor() - 1);
        view.updateButtonLight(button, true);
      }
    }
  }
}



