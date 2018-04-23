import ElSys.CabinRequests;
import ElSys.Enums.ButtonLight;
import ElSys.FloorRequest;
import ElSys.SimButton;

import java.util.List;
import java.util.Random;
import java.util.Set;
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
    
    for(int i = 0; i < numFloors; i++)
    {
      buttons[i] = new SimButton(Integer.toString(i));
    }
    
    final CabinRequests cr = new CabinRequests(buttons);

    IntStream.range(0, numFloors).forEach(i -> cr.setButtonLight(ButtonLight.ON, i));

    final Set<FloorRequest> results = cr.updateRequests();
    assert results.size() == numFloors;
  }
}
