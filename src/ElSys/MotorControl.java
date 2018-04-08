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
    long t = 1000/60;

    for (int i=0; i<60; i++)
    {
      simPhysLocation.move(step);
      try
      {
        Thread.sleep(t);
      } catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }
  }
}