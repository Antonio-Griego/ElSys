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
    final SimButton[] buttons = new SimButton[10];
    final Random random = new Random();
  
    for(int i = 0; i < buttons.length; i++)
    {
      buttons[i] = new SimButton(random);
    }
    
    Cabin cabin = new Cabin(buttons, new SimPhysLocation(10));
    
    cabin.start();
//    Motion motion = new Motion(new SimPhysLocation(10));
//    motion.setDirection(CabinDirection.UP);
//    int floor = -1;
//
//    while(true)
//    {
//      if(floor != motion.getFloor())
//      {
//        floor = motion.getFloor();
//        System.out.println(floor);
//      }
//
//      if(floor == 7 && motion.getDirection() != CabinDirection.STOPPED) motion.stopElevator();
//    }
  }
}
