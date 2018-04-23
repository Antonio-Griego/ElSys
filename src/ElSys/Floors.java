package ElSys;

import ElSys.Enums.ButtonLight;
import ElSys.Enums.CabinDirection;


import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Floors
{
  private final Button[] up_Buttons;
  private final Button[] down_Buttons;
  private final ArrivalSignal[] up_Signals;
  private final ArrivalSignal[] down_Signals;
  
  public Floors(Button[] up_Buttons, Button [] down_Buttons, ArrivalSignal[] up_Signals, ArrivalSignal[] down_Signals)
  {
    this.up_Buttons = up_Buttons;
    this.down_Buttons = down_Buttons;
    this.up_Signals = up_Signals;
    this.down_Signals = down_Signals;
  }
  
  public Set<FloorRequest> getRequests()
  {
    Set<FloorRequest> requests = new HashSet<>();
    
    for(int i = 0; i < (up_Buttons.length)-1; i++)
    {
      if(up_Buttons[i].getLight() == ButtonLight.ON) requests.add(new FloorRequest(i, CabinDirection.UP));
    }
  
    for(int i = 1; i < down_Buttons.length; i++)
    {
      if(down_Buttons[i].getLight() == ButtonLight.ON) requests.add(new FloorRequest(i+1, CabinDirection.DOWN));
    }
    
    return requests;
  }
  
  public void resetButton(int floor, CabinDirection direction)
  {

//    if(direction == CabinDirection.UP) up_Buttons[floor].setLight(ButtonLight.OFF);
//    else down_Buttons[floor].setLight(ButtonLight.OFF);
    
    if(direction == CabinDirection.UP && floor < 9)
    {
      up_Buttons[floor].setLight(ButtonLight.OFF);
    }
    else if(direction == CabinDirection.DOWN && floor > 0)
    {
      down_Buttons[floor].setLight(ButtonLight.OFF);
    }

  }
  
  public void setArrivalSignal(int floor, CabinDirection direction, boolean lightOn)
  {
    if(direction == CabinDirection.UP && floor < 9)
    {
      up_Signals[floor].setArrivalSignal(lightOn);
    }
    else if(direction == CabinDirection.DOWN && floor > 0)
    {
      down_Signals[floor].setArrivalSignal(lightOn);
    }
  }

  public ArrivalSignal[] getDown_Signals() { return down_Signals; }

  public ArrivalSignal[] getUp_Signals() { return up_Signals; }
}
