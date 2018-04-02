import ElSys.Cabin;
import ElSys.Enums.CabinDirection;
import ElSys.FloorRequest;
import ElSys.SimPhysLocation;

/**
 * Unit tests for {@link Cabin}
 */
class CabinTest
{
  CabinTest()
  {
    testRequestWithDirectionFromStop();
    testRequestWithoutDirectionFromStop();
  }

  private void testRequestWithDirectionFromStop()
  {
    final Cabin cabin = new Cabin(10, new SimPhysLocation(10));
    final CabinDirection cabinDirection = CabinDirection.UP;

    cabin.addRequest(new FloorRequest(5, cabinDirection));
    cabin.run();

    assert cabin.getStatus().getDirection() == cabinDirection;
  }

  private void testRequestWithoutDirectionFromStop()
  {
    final Cabin cabin = new Cabin(10, new SimPhysLocation(10));

    cabin.addRequest(new FloorRequest(5, null));
    cabin.run();

    assert cabin.getStatus().getDirection() == CabinDirection.UP;
  }
}
