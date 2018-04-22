import ElSys.*;
import ElSys.Enums.ButtonLight;
import ElSys.Enums.CabinDirection;

import java.util.Queue;
import java.util.Set;

public class FloorsTest
{
  private ArrivalSignal[] up_Sig;
  private ArrivalSignal[] down_Sig;
  private Button[] up_Button;
  private Button[] down_Button;
  private Floors floors;
  
  final int FLOORS = 10;
  
  public FloorsTest()
  {
    up_Sig = new ArrivalSignal[FLOORS-1];
    down_Sig = new ArrivalSignal[FLOORS-1];
    up_Button = new Button[FLOORS-1];
    down_Button = new Button[FLOORS-1];
    
    
    for(int i = 0; i < FLOORS-1; i++)
    {
      down_Sig[i] = new ArrivalSignal(new SimSignal(CabinDirection.DOWN, i+1));
      down_Button[i] = new Button(new SimButton("Floor "+(i+1)+" DOWN button"));
      up_Sig[i] = new ArrivalSignal(new SimSignal(CabinDirection.UP, i));
      up_Button[i] = new Button(new SimButton("Floor "+i+" UP button"));
    }
    
    floors = new Floors(up_Button, down_Button, up_Sig, down_Sig);
    runTest();
  }
  
  private void runTest()
  {
    up_Button[0].setLight(ButtonLight.ON);
    up_Button[3].setLight(ButtonLight.ON);
    up_Button[5].setLight(ButtonLight.ON);
    down_Button[4].setLight(ButtonLight.ON);
    down_Button[2].setLight(ButtonLight.ON);
    down_Button[6].setLight(ButtonLight.ON);
    printRequests();
    floors.resetButton(0, CabinDirection.UP);
    floors.resetButton(3, CabinDirection.DOWN);
    printRequests();
    floors.setArrivalSignal(1, CabinDirection.DOWN, true);
    floors.setArrivalSignal(3, CabinDirection.UP, true);
    floors.setArrivalSignal(1, CabinDirection.DOWN, false);
    floors.setArrivalSignal(3, CabinDirection.UP, true);
  }
  
  private void printRequests()
  {
    Set<FloorRequest> requests = floors.getRequests();
    for(FloorRequest r : requests)
    {
      r.print();
    }
  }
}
