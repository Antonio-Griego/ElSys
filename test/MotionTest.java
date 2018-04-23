import ElSys.*;
import ElSys.Enums.CabinDirection;

import java.util.Random;

public class MotionTest
{
  private final Motion motion;
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
    motion = new Motion(new SimPhysLocation(2, "Elevator"));
//    goToFloor(2);
//    goToFloor(3);
//    goToFloor(7);
    goToFloor(18);
    
  }
  
  
  private void goToFloor(int floor)
  {
    motion.setDestination(floor);
  
    while(motion.getDestination() != null)
    {
      if(motion.getDestination() == null) break;
    }
  
    System.out.println(motion.isAligned());
  }
}
