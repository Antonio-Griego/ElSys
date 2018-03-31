package ElSys;

public class MotorControl
{
  SimPhysLocation simPhysLocation;

  MotorControl(SimPhysLocation simPhysLocation)
  {
    this.simPhysLocation = simPhysLocation;
  }

  void moveElevator(int newFloor)
  {
    simPhysLocation.move(newFloor);
  }
}