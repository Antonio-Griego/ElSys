package ControlPanel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ControlPanelCabin
{
  Tab tab;

  @FXML
  Button buttonOne, buttonTwo, buttonThree, buttonFour;

  public ControlPanelCabin(int cabinNumber)
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

    buttonOne.setOnAction(this::buttonPressed);
  }

  private void buttonPressed(ActionEvent event)
  {
    Button button = ((Button)event.getSource());

  }

  protected Tab getTab(){return tab;}
}
