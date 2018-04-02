import ElSys.Enums.CabinDirection;
import ElSys.FloorRequest;

class FloorRequestTest
{
  FloorRequestTest()
  {
    equalityTest();
  }

  private void equalityTest()
  {
    final FloorRequest a1n = new FloorRequest(1, null);
    final FloorRequest a2n = new FloorRequest(2, null);

    final FloorRequest b1n = new FloorRequest(1, null);
    final FloorRequest a2u = new FloorRequest(2, CabinDirection.UP);

    assert a1n.equals(b1n);
    assert !a2n.equals(a2u);
    assert !a1n.equals(a2n);
  }
}
