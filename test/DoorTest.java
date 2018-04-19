import ElSys.Door;
import ElSys.Doors;
import ElSys.SimDoor;

public class DoorTest
{
  private Doors doors;
  
  public DoorTest()
  {
    doors = makeDoors(10);
    runTest();
  }
  
  private Doors makeDoors(int floors)
  {
    Door cabinDoor = new Door(new SimDoor());
    Door[] floorDoors = new Door[floors];
    for(int i = 0; i < floors; i++)
    {
      floorDoors[i] = new Door(new SimDoor());
    }
    
    return new Doors(floorDoors, cabinDoor);
  }
  
  private void runTest()
  {
    System.out.println(doors.areClosed());
    doors.openDoors(0);
    while(!doors.areOpen())
    {
    }
    System.out.println(doors.areClosed());
    doors.closeDoors(0);
    while(!doors.areClosed())
    {
    }
    System.out.println(doors.areClosed());
    System.out.println(doors.areClosed());
    System.out.println(doors.areClosed());
    System.out.println(doors.areClosed());
  }
}
