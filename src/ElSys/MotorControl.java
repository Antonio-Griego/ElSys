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
    double step = distance/60;
    double t = 1000/60;

    for (int i=0; i<60; i++)
    {
      simPhysLocation.move(step);
      sleep(t);
    }
  }
}