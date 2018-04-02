package ElSys;

import ElSys.Enums.ButtonLight;

public class Button
{
  private boolean isPressed;
  private ButtonLight buttonLight;
  private final SimButton simButton;

  Button(final SimButton simButton)
  {
    this.simButton = simButton;
  }

  public void setLight(ButtonLight buttonLight)
  {
    this.buttonLight = buttonLight;
    
    if(buttonLight == ButtonLight.OFF)
    {
      isPressed = false;
    }
  }

  public ButtonLight getLight()
  {
    return buttonLight;
  }

  public boolean isPressed()
  {
    isPressed = simButton.isPressed();
    if(isPressed) buttonLight = ButtonLight.ON;
    return isPressed;
  }
}