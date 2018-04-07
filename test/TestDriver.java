import javafx.application.Application;
import javafx.stage.Stage;

// Note that -ea vm flag is required for assertions to work
public class TestDriver extends Application
{
  public static void main(String[] args)
  {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage)
  {
    new CabinTest();
    new CabinRequestsTest();
    new FloorRequestTest();

//    new ElSysTest();
//
//    new ControlPanelTest();
//
//    new MotionTest();
  }
}
