package ElSys;

import java.math.BigDecimal;

public class MotorControl
{
  private SimPhysLocation simPhysLocation;

  MotorControl(SimPhysLocation simPhysLocation)
  {
    this.simPhysLocation = simPhysLocation;
  }

  void moveElevator(BigDecimal distance)
  {
    BigDecimal distanceMoved = new BigDecimal("0.0");
    BigDecimal step = new BigDecimal(Double.toString(distance.doubleValue()/60));
    long t = 1000/60;

    for (int i=0; i<60; i++)
    {
      simPhysLocation.move(step);
      distanceMoved = distanceMoved.add(step);
      try
      {
        Thread.sleep(t);
      } catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }
    
    if(distance.subtract(distanceMoved).abs().doubleValue() > 0)
    {
      if(distance.doubleValue() > 0)
      {
        simPhysLocation.move(distance.subtract(distanceMoved));
      }
      
      else simPhysLocation.move(distanceMoved.subtract(distance));
    }
  }
}