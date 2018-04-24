package ElSys;

import ElSys.Enums.ButtonLight;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class represents a simulated button in an elevator cabin.
 *
 * @author Antonio Griego
 */
public class SimButton
{
  private AtomicBoolean buttonLight;
  private final String name;

  /**
   * Constructor method.
   */
  public SimButton(String name)
  {
    this.name = name;
    this.buttonLight = new AtomicBoolean(false);
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
      if(!buttonLight.get())
      {
        System.out.println(name + ": ButtonLight = ON");
      }
      buttonLight.set(isButtonLightOn);
    }
    
    else
    {
      System.out.println(name + ": ButtonLight = OFF");
      buttonLight.set(isButtonLightOn);
    }
  }

  public boolean getLight()
  {
    return buttonLight.get();
  }
}