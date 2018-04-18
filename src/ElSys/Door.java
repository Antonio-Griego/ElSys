package ElSys;

/**
 * Created by glennon on 4/18/18.
 */
public class Door
{
  private SimDoor simDoor;
  private double doorVal;
  // add enums for doorstate



  // init simDoor, doorVal to closed, DoorState to closed
  public Door(SimDoor simDoor)
  {
    this.simDoor = simDoor;
    doorVal = 1;
    // set enum to closed
  }



  public void actuateDoor(double amount)
  {
    // actuate door by specified amount
    simDoor.moveDoor(amount);
    doorVal = doorVal + amount;
    if (doorVal > 1)
    {
      doorVal=1;
    }
    if (doorVal < 0)
    {
      doorVal = 0;
    }

    // change enum
  }

  //need to add enums
  public DoorState getDoorState()
  {
    // return door state enum
    return null;
  }
}
