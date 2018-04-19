package ElSys;

import ElSys.Enums.ButtonLight;

public class Button
{
  private final SimButton simButton;

  public Button(final SimButton simButton)
  {
    this.simButton = simButton;
  }

  public void setLight(final ButtonLight buttonLight)
  {
    simButton.setLight(buttonLight == ButtonLight.ON);
  }

  public ButtonLight getLight()
  {
    return simButton.getLight();
  }
}