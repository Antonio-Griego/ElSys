package ElSys;

/**
 * Created by glennon on 4/18/18.
 */
public class Door
{
  private SimDoor simDoor;
  private int doorVal;
  private DoorState doorState;
  // add enums for doorstate
  
  public enum DoorState{
    OPEN,
    OPENING,
    CLOSING,
    CLOSED
  }



  // init simDoor, doorVal to closed, DoorState to closed
  public Door(SimDoor simDoor)
  {
    this.simDoor = simDoor;
    doorVal = 100;
    doorState = DoorState.CLOSED;
    simDoor.moveDoor(doorVal);
  }



  public void actuateDoor(int amount)
  {
    // actuate door by specified amount
    simDoor.moveDoor(amount);
    doorVal = doorVal + amount;
    if (doorVal >= 100)
    {
      doorVal=100;
      doorState = DoorState.CLOSED;
    }
    
    if (doorVal <= 0)
    {
      doorVal = 0;
      doorState = DoorState.OPEN;
    }
  }
  
  public DoorState getDoorState()
  {
    return doorState;
  }
  
  public void setDoorState(DoorState state)
  {
    this.doorState = state;
  }

  public int getDoorVal(){return doorVal;}
}
