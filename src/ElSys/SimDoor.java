package ElSys;

/**
 * Created by glennon on 4/18/18.
 */
public class SimDoor
{
  // open = 0    closed = 1
  int doorVal;
  String name = "";

  public SimDoor(String name)
  {
    this.name = name;
    doorVal = 100;
  }
  // door initialized to closed
  public SimDoor()
  {
    doorVal=100;
  }

  // positive = close
  // negative = open
  public void moveDoor(int amount)
  {
    doorVal = doorVal + amount;
    if (doorVal >= 100)
    {
      doorVal = 100;
    }
    if (doorVal <= 0)
    {
      doorVal = 0;
    }
    
    //For Debugging
    if(!name.equals("")) printStatus();
  }
  
  //For Debugging
  private void printStatus()
  {
    String status;
    if(doorVal >= 100) status = "CLOSED";
    else if(doorVal <= 0) status = "OPEN";
    else return;
    
    System.out.println(name+" doors are "+status);
  }
}
