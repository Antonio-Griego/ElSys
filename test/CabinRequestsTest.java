import ElSys.CabinRequests;
import ElSys.Enums.ButtonLight;
import ElSys.FloorRequest;

import java.util.List;
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
    final CabinRequests cr = new CabinRequests(numFloors);

    IntStream.range(0, numFloors).forEach(i -> cr.setButtonLight(ButtonLight.ON, i));

    final List<FloorRequest> results = cr.updateRequests();

    boolean err = IntStream.range(0, numFloors)
            .filter(idx -> results.get(idx).getFloor() != idx)
            .count() > 0;

    assert !err && results.size() == numFloors;
  }
}
