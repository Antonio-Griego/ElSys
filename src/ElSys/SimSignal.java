package ElSys;

import ElSys.Enums.CabinDirection;

public class SimSignal
{
  final int floor;
  final CabinDirection direction;
  private boolean lightOn;
  public SimSignal(CabinDirection direction, int floor)
  {
    this.floor = floor;
    this.direction = direction;
    lightOn = false;
  }
  
  public void setSignal(boolean lightOn)
  {
    if(this.lightOn != lightOn)
    {
      this.lightOn = lightOn;
      String status;
      if(lightOn) status = "ON";
      else status = "OFF";
      
      System.out.println("Floor "+floor+" "+direction+" signal is now "+status);
    }
  }
}
