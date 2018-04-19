package ElSys;

import java.math.BigDecimal;

/**
 * Created by glennon on 4/18/18.
 */
public class Doors extends Thread
{
  private Door [] floorDoors;
  private Door cabinDoor;
  private final int DOOR_SPEED = 10;

  public Doors(Door [] floorDoors, Door cabinDoor)
  {
    //init door objects
    this.floorDoors = floorDoors;
    this.cabinDoor = cabinDoor;
    this.start();
  }



  // how much to open door at a time?
  public synchronized void openDoors(int floor)
  {
    if(floor < floorDoors.length)
    {
      cabinDoor.setDoorState(Door.DoorState.OPENING);
      floorDoors[floor].setDoorState(Door.DoorState.OPENING);
    }
    else
    {
      System.out.println("Invalid entry");
    }
  }

  // how much to close a door at a time?
  public synchronized void closeDoors(int floor)
  {
    if (floor < floorDoors.length)
    {
      cabinDoor.setDoorState(Door.DoorState.CLOSING);
      floorDoors[floor].setDoorState(Door.DoorState.CLOSING);
    }
    else
    {
      System.out.println("Invalid entry");
    }
  }

  public boolean areClosed()
  {
    if(cabinDoor.getDoorState() != Door.DoorState.CLOSED) return false;
    
    for(Door door : floorDoors)
    {
      if(door.getDoorState() != Door.DoorState.CLOSED) return false;
    }
    
    return true;
  }
  
  public boolean areOpen()
  {
    if(cabinDoor.getDoorState() == Door.DoorState.OPEN) return true;
  
    for(Door door : floorDoors)
    {
      if(door.getDoorState() == Door.DoorState.OPEN) return true;
    }
  
    return false;
  }
  
  @Override
  public void run()
  {
    while (true)
    {
      if(cabinDoor.getDoorState() != Door.DoorState.CLOSED && cabinDoor.getDoorState() != Door.DoorState.OPEN)
      {
        int i;
        
        for(i = 0; i < floorDoors.length; i++)
        {
          if(floorDoors[i].getDoorState() != Door.DoorState.CLOSED && floorDoors[i].getDoorState() != Door.DoorState.OPEN) break;
        }
        
        int distance = DOOR_SPEED;
        if(cabinDoor.getDoorState() == Door.DoorState.OPENING) distance = 0-distance;
        
        
        floorDoors[i].actuateDoor(distance);
        cabinDoor.actuateDoor(distance);
      }
    }
  }
}
