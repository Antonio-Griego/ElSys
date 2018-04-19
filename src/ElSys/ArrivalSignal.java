package ElSys;

public class ArrivalSignal
{
  private boolean lightOn;
  private final SimSignal simSignal;
  
  public ArrivalSignal(SimSignal simSignal)
  {
    lightOn = false;
    simSignal.setSignal(lightOn);
    this.simSignal = simSignal;
  }
  
  public void setArrivalSignal(boolean lightOn)
  {
    this.lightOn = lightOn;
    simSignal.setSignal(lightOn);
  }
  
  public boolean isLightOn()
  {
    return lightOn;
  }
}
