package ElSys;

import ElSys.Enums.ButtonLight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Contains a {@link Cabin}s requests and the buttons associated with said cabin.
 *
 * Polls buttons for their activation status, then updates the list of current requests
 */
public class CabinRequests
{
  private ArrayList<Button> cabinButtons = new ArrayList<>();

  /**
   * Create a CabinRequest, which will poll {@code simButtons}, generating requests based on their activity
   */
  public CabinRequests(final SimButton[] simButtons)
  {
    // Construct buttons using simButtons, then add them to cabinButtons
    Arrays.stream(simButtons)
            .forEach(sb -> cabinButtons.add(new Button(sb)));
  }

  /**
   * Poll buttons for activation, generating a list of requests based on the results.
   */
  public Set<FloorRequest> updateRequests()
  {
    final Set<Button> activeButtons = cabinButtons.stream()
            .filter(button -> (button.getLight() == ButtonLight.ON))
            .collect(Collectors.toSet());

    return generateFloorRequests(activeButtons);
  }

  /**
   * Set the button light for the button on the floor specified by {@code floorNumber} to the value
   * specified by {@code buttonLight}.
   */
  public void setButtonLight(final ButtonLight buttonLight, final int floorNumber)
  {
    cabinButtons.get(floorNumber).setLight(buttonLight);
  }

  private Set<FloorRequest> generateFloorRequests(final Set<Button> activeButtons)
  {
    return activeButtons.stream()
            .map(b -> new FloorRequest(cabinButtons.indexOf(b), null))
            .collect(Collectors.toSet());
  }
}