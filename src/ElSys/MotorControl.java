package ElSys;

public class MotorControl
{
  private SimPhysLocation simPhysLocation;

  MotorControl(SimPhysLocation simPhysLocation)
  {
    this.simPhysLocation = simPhysLocation;
  }

  void moveElevator(double distance)
  {
    simPhysLocation.move(distance);
  }
}