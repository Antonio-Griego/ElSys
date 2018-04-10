import ElSys.Cabin;
import ElSys.Enums.CabinDirection;
import ElSys.Motion;
import ElSys.SimButton;
import ElSys.SimPhysLocation;

import java.util.Random;

public class MotionTest
{
  public MotionTest()
  {
    //    final SimButton[] buttons = new SimButton[10];
    //    final Random random = new Random();
    //
    //    for(int i = 0; i < buttons.length; i++)
    //    {
    //      buttons[i] = new SimButton(random, i);
    //    }
    //
    //    Cabin cabin = new Cabin(buttons, new SimPhysLocation(10));
    //
    //    cabin.start();
    Motion motion = new Motion(new SimPhysLocation(10));
    motion.setDestination(0);
    motion.setDestination(9);
    try
    {
      Thread.sleep(10000);
    } catch (InterruptedException e)
    {
      e.printStackTrace();
    }
    motion.setDestination(1);
  }
}
