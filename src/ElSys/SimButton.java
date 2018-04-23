package ElSys;

import ElSys.Enums.ButtonLight;

/**
 * This class represents a simulated button in an elevator cabin.
 *
 * @author Antonio Griego
 */
public class SimButton
{
  private ButtonLight buttonLight;
  private final String name;

  /**
   * Constructor method.
   */
  public SimButton(String name)
  {
    this.name = name;
    this.buttonLight = ButtonLight.OFF;
  }

  /**
   * This method sets the status of the button light: true = on, false = off.
   * The status of the light is printed out to the console and the button
   * press status is updated: light on = button is pressed, light off = button
   * is not pressed.
   *
   * @param isButtonLightOn
   */
  public void setLight(boolean isButtonLightOn)
  {
    if(isButtonLightOn)
    {
      if(buttonLight != ButtonLight.ON)
      {
        System.out.println(name + ": ButtonLight = ON");
      }
      this.buttonLight = ButtonLight.ON;
    }
    
    else
    {
      System.out.println(name + ": ButtonLight = OFF");
      this.buttonLight = ButtonLight.OFF;
    }
  }

  public ButtonLight getLight()
  {
    return buttonLight;
  }
}