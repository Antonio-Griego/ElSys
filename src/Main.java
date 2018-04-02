import ControlPanel.ControlPanel;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application
{
  int floors = 10;
  int cabins = 4;

  @Override
  public void start(Stage primaryStage) throws Exception
  {
    ControlPanel controlPanel = new ControlPanel(floors, cabins);
  }

  public static void main(String[] args)
  {
    launch(args);
  }
}