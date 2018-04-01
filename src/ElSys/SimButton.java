package ElSys;

import ElSys.Enums.ButtonLight;
import java.util.Random;

/**
 * This class represents a simulated button in an elevator cabin.
 *
 * @author Antonio Griego
 */
public class SimButton
{
  private boolean isPressed;
  private ButtonLight buttonLight;
  private Random random;

  /**
   * Constructor method.
   *
   * @param random Random number generator object.
   */
  SimButton(Random random)
  {
    this.isPressed = false;
    this.buttonLight = ButtonLight.OFF;
    this.random = random;
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
      System.out.println("ButtonLight = ON");
      this.buttonLight = ButtonLight.ON;
      isPressed = true;
    }
    else
    {
      System.out.println("ButtonLight = OFF");
      this.buttonLight = ButtonLight.OFF;
      isPressed = false;
    }
  }

  /**
   * This method returns the pressed status of the button: true = pressed,
   * false = not pressed. If the button is NOT pressed, there is a 10% chance
   * that the button will change state and become pressed.
   *
   * @return True = button is pressed; False = button is not pressed
   */
  public boolean isPressed()
  {
    // change state if and only if isPressed = false
    if(!isPressed)
    {
      isPressed = (random.nextDouble() <= 0.1);
    }

    return isPressed;
  }
}