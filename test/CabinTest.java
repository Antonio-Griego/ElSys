import ElSys.Cabin;
import ElSys.Enums.CabinDirection;
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
    testSetDestinationFromStop();
  }

  private void testSetDestinationFromStop()
  {
    final SimButton [] buttons = new SimButton[10];
  
    for(int i = 0; i < buttons.length; i++)
    {
      buttons[i] = new SimButton(i);
    }
    
    final Cabin cabin = new Cabin(buttons, new SimPhysLocation(10));
    final CabinDirection cabinDirection = CabinDirection.UP;

    cabin.setDestination(5);
    cabin.run();

    assert cabin.getStatus().getDirection() == cabinDirection;
  }
}
