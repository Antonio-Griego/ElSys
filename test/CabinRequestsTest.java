import ElSys.CabinRequests;
import ElSys.Enums.ButtonLight;
import ElSys.FloorRequest;
import ElSys.SimButton;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

class CabinRequestsTest
{
  CabinRequestsTest()
  {
    oneRequestPerFloorTest();
  }

  private void oneRequestPerFloorTest()
  {
    final int numFloors = 10;
    final SimButton [] buttons = new SimButton[numFloors];
    final Random random = new Random();
    
    for(int i = 0; i < numFloors; i++)
    {
      buttons[i] = new SimButton(random);
    }
    
    final CabinRequests cr = new CabinRequests(buttons);

    IntStream.range(0, numFloors).forEach(i -> cr.setButtonLight(ButtonLight.ON, i));

    final List<FloorRequest> results = cr.updateRequests();

    boolean err = IntStream.range(0, numFloors)
            .filter(idx -> results.get(idx).getFloor() != idx)
            .count() > 0;

    assert !err && results.size() == numFloors;
  }
}
