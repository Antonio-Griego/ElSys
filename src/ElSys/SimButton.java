package ElSys;

import ElSys.Enums.ButtonLight;

public class SimButton
{
  private boolean isPressed = false;
  private ButtonLight buttonLight = ButtonLight.OFF;

  public void setLight(ButtonLight buttonLight)
  {
    this.buttonLight = buttonLight;
  }

  public ButtonLight getLight()
  {
    return buttonLight;
  }

  public boolean isPressed()
  {
    return isPressed;
  }

  public void press()
  {
    setLight(ButtonLight.ON);
    isPressed = true;
  }

  public void reset()
  {
    buttonLight = ButtonLight.OFF;
    isPressed = false;
  }
}