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
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Polygon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ControlPanelCabin
{
  private int currentFloor = 1;
  private CabinMode mode;
  private Tab tab;
  private CabinDirection currDirection = CabinDirection.STOPPED;
  private ArrayList<Button> floors = new ArrayList<>();
  private ArrayList<Button> cabinButtons = new ArrayList<>();

  @FXML
  Button buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive, buttonSix,
          buttonSeven, buttonEight, buttonNine, buttonTen,
          floorOne, floorTwo, floorThree, floorFour, floorFive, floorSix, floorSeven,
          floorEight, floorNine, floorTen;

  @FXML
  Polygon upArrow, downArrow;

  //TODO: For testing. Remove when complete
  ControlPanelCabin(int cabinNumber)
  {
    loadView(cabinNumber);
    addFloors();
    addButtons();
  }

  //TODO refactor to contain view
  ControlPanelCabin(CabinStatus cabinStatus, int cabinNumber)
  {
    loadView(cabinNumber);

    //TODO allow for variable floor #'s. Loaded dynamically
    addFloors();
    addButtons();
    update(cabinStatus);
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

    cabinButtons.forEach(button -> button.setOnAction(this::buttonPressed));
  }

  private void buttonPressed(ActionEvent event)
  {
    Button button = ((Button)event.getSource());
    updateButtonLight(button, true);
  }

  private void loadView(int cabinNumber)
  {
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
  }

  protected void update(CabinStatus cabinStatus)
  {
//    List<FloorRequest> floorRequests = new ArrayList<>(cabinStatus.getFloorRequests());

    updateFloors(cabinStatus.getFloor());
    updateDirection(cabinStatus.getDirection());
//    updateButtons(floorRequests);
    mode = cabinStatus.getMode();
  }

  private void updateButtons(List<FloorRequest> floorRequests)
  {
    cabinButtons.forEach(button -> updateButtonLight(button, false));

    for(FloorRequest request: floorRequests)
    {
      Button button = cabinButtons.get(request.getFloor()-1);
      updateButtonLight(button, true);
    }
  }

  private void updateButtonLight(Button button, boolean turnOn)
  {
      if (turnOn)
      {
        Platform.runLater(()->button.getStyleClass().add("active-cabin-button"));
      }
      else
      {
        Platform.runLater(()->button.getStyleClass().add("inactive-cabin-button"));
      }
  }

  private void updateDirection(CabinDirection newDirection)
  {
    switch (newDirection)
    {
      case STOPPED:
        updateDirectionLight(downArrow, false);
        updateDirectionLight(upArrow, false);
        break;

      case UP:
        updateDirectionLight(downArrow, false);
        updateDirectionLight(upArrow, true);
        break;

      case DOWN:
        updateDirectionLight(downArrow, true);
        updateDirectionLight(upArrow, false);
    }

    currDirection = newDirection;
  }

  private void updateDirectionLight(Polygon arrow, boolean turnOn)
  {
    if (turnOn)
    {

      Platform.runLater(() -> arrow.getStyleClass().add("active-arrow"));
    }
    else
    {

      Platform.runLater(() -> arrow.getStyleClass().add("inactive-arrow"));
    }

  }

  private void updateFloors(int floor)
  {
    if(floor != currentFloor)
    {

      updateFloorLight(floors.get(currentFloor), false);
      updateFloorLight(floors.get(floor), true);

      currentFloor = floor;
    }
  }

  private void updateFloorLight(Button button, boolean turnOn)
  {

    if (turnOn)
    {
      Platform.runLater(() ->
                        {
                          button.getStyleClass().clear();
                          button.getStyleClass().add("active-floor");
                        });
    }
    else
    {
      Platform.runLater(() ->
                        {
                          button.getStyleClass().clear();
                          button.getStyleClass().add("inactive-floor");
                        });
    }

  }

  protected Tab getTab(){return tab;}

  protected CabinMode getMode(){return mode;}
}
