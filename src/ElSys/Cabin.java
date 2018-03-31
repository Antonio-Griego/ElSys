package ElSys;

public class Cabin
{
  private CabinStatus cabinStatus;
  private CabinRequests cabinRequests;
  private Motion motion;
  private final int numberOfFloors;

  Cabin(int numberOfFloors, SimPhysLocation simPhysLocation)
  {
    this.cabinStatus = new CabinStatus();
    this.numberOfFloors = numberOfFloors;
    this.cabinRequests = new CabinRequests(numberOfFloors);
    this.motion = new Motion(simPhysLocation);
  }

  public void run()
  {
    System.out.println("cabin.run()");
  }

  public void addRequest()
  {
    System.out.println("cabin.addRequest()");
  }

  public void getStatus()
  {
    System.out.println("cabin.getStatus()");
  }

  public void updateMode()
  {
    System.out.println("cabin.updateMode()");
  }

  public void hasArrived()
  {
    System.out.println("cabin.hasArrived()");
  }
}