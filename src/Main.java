import ElSys.ControlPanel.ControlPanel;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application
{

  @Override
  public void start(Stage primaryStage) throws Exception
  {
    //Used to load GUI (testing only)
    ControlPanel controlPanel = new ControlPanel(4, 4);
  }

  public static void main(String[] args)
  {
    launch(args);
  }
}