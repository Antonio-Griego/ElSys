package ElSys;

import ElSys.Enums.ButtonLight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Contains a {@link Cabin} cabins requests and the buttons associated with said cabin.
 *
 * Polls buttons for their activation status, then updating the list of current requests
 */
public class CabinRequests
{
  private ArrayList<Button> cabinButtons = new ArrayList<>();

  public CabinRequests(SimButton [] simButtons)
  {
    Collections.nCopies(simButtons.length, null).forEach(n -> cabinButtons.add(new Button(simButtons[cabinButtons.size()])));
  }

  public List<FloorRequest> updateRequests()
  {
    final Set<Button> activeButtons = cabinButtons.stream()
            .filter(button -> (button.isPressed() || button.getLight() == ButtonLight.ON))
            .collect(Collectors.toSet());
    

    activeButtons.forEach(b -> b.setLight(ButtonLight.ON));
    return generateFloorRequests(activeButtons);
  }

  public void setButtonLight(final ButtonLight buttonLight, final int floorNumber)
  {
    cabinButtons.get(floorNumber).setLight(buttonLight);
  }

  public void satisfyRequest(final FloorRequest req)
  {
    cabinButtons.get(req.getFloor()).setLight(ButtonLight.OFF);
  }

  private List<FloorRequest> generateFloorRequests(final Set<Button> activeButtons)
  {
    return activeButtons.stream()
            .map(b -> new FloorRequest(cabinButtons.indexOf(b), null))
            .collect(Collectors.toList());
  }
}