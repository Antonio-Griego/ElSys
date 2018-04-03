import ElSys.Cabin;
import ElSys.Enums.CabinDirection;
import ElSys.FloorRequest;
import ElSys.SimButton;
import ElSys.SimPhysLocation;

import java.util.Random;

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
    final SimButton [] buttons = new SimButton[10];
    final Random random = new Random();
  
    for(int i = 0; i < buttons.length; i++)
    {
      buttons[i] = new SimButton(random);
    }
    
    final Cabin cabin = new Cabin(buttons, new SimPhysLocation(10));
    final CabinDirection cabinDirection = CabinDirection.UP;

    cabin.addRequest(new FloorRequest(5, cabinDirection));
    cabin.run();

    assert cabin.getStatus().getDirection() == cabinDirection;
  }

  private void testRequestWithoutDirectionFromStop()
  {
    final SimButton [] buttons = new SimButton[10];
    final Random random = new Random();
  
    for(int i = 0; i < buttons.length; i++)
    {
      buttons[i] = new SimButton(random);
    }
    
    final Cabin cabin = new Cabin(buttons, new SimPhysLocation(10));

    cabin.addRequest(new FloorRequest(5, null));
    cabin.run();

    assert cabin.getStatus().getDirection() == CabinDirection.UP;
  }
}
