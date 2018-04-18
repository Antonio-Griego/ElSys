package ElSys;

/**
 * Created by glennon on 4/18/18.
 */
public class Doors extends Thread
{
  private Door [] floorDoors;
  private Door cabinDoor;


  public Doors(Door [] floorDoors, Door cabinDoor)
  {
    //init door objects
    this.floorDoors = floorDoors;
    this.cabinDoor = cabinDoor;
  }



  // how much to open door at a time?
  public void openDoors(int floor)
  {
    //open cabin door and floordoor
    cabinDoor.actuateDoor(-1);
    floorDoors[floor].actuateDoor(-1);

    // set enum to opening
  }

  // how much to close a door at a time?
  public void closeDoors(int floor)
  {
    //close cabin door and floordoor
    cabinDoor.actuateDoor(1);
    floorDoors[floor].actuateDoor(1);

    //set enum to closing
  }

  public boolean areClosed()
  {
    // return true if all doors are closed
    // return false if any door is not closed
    return true;
  }



  @Override
  public void run()
  {
    while (true)
    {
      // loop over procedure that checks state of each door and actuates accordingly


      // if opening: actuate cabin and floor door in a synchoronized fashion
      // same for closing

      // if open, wait 3 seconds then closes

      // if closed does nothing
    }
  }
}
