import ElSys.BuildingControl;
import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

public class TestDriver extends Application
{
  public static void main(String[] args)
  {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception
  {
    //    new CabinTest();
//    new CabinRequestsTest();
//    new FloorRequestTest();

    new ElSysTest();

//    new ControlPanelTest();

    //new MotionTest();
  }
}
