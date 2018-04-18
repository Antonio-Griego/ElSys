package ElSys;

/**
 * Created by glennon on 4/18/18.
 */
public class SimDoor
{
  // open = 0    closed = 1
  double doorVal;


  // door initialized to closed
  public SimDoor()
  {
    doorVal=1;
  }

  // positive = close
  // negative = open
  public void moveDoor(double amount)
  {
    doorVal = doorVal + amount;
    if (doorVal > 1)
    {
      doorVal=1;
    }
    if (doorVal < 0)
    {
      doorVal = 0;
    }
  }
}
