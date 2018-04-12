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
  private int floor;

  /**
   * Constructor method.
   *
   * @param random Random number generator object.
   */
  public SimButton(Random random, int floor)
  {
    this.isPressed = false;
    this.buttonLight = ButtonLight.OFF;
    this.random = random;
    this.floor = floor;

    if (floor % 3 == 0 && floor != 0) isPressed = true;
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
        System.out.println("Floor "+floor+": ButtonLight = ON");
      }
      this.buttonLight = ButtonLight.ON;
    }
    
    else
    {
      System.out.println("Floor "+floor+": ButtonLight = OFF");
      this.buttonLight = ButtonLight.OFF;
      isPressed = false;
    }
  }

  /**
   * This method returns the pressed status of the button: true = pressed,
   * false = not pressed. If the button is NOT pressed, there is a 0.00001% chance
   * that the button will change state and become pressed.
   *
   * @return True = button is pressed; False = button is not pressed
   */
  public boolean isPressed()
  {
    // change state if and only if isPressed = false
    if(!isPressed)
    {
      isPressed = (random.nextDouble() <= 0.0000001);
    }

    return isPressed;
  }
}