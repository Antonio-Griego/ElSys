package ElSys;

import ElSys.Enums.ButtonLight;
import java.util.ArrayList;

public class CabinRequests
{
  private final int numberOfFloors;
  private ArrayList<Button> cabinButtons = new ArrayList<>();
  private ArrayList<Boolean> requests = new ArrayList<>();

  CabinRequests(int numberOfFloors)
  {
    this.numberOfFloors = numberOfFloors;

    for(int i = 0; i < numberOfFloors; i++)
    {
      cabinButtons.add(new Button());
    }
  }

  public ArrayList<Boolean> updateRequests()
  {
    requests.clear();

    for(Button b : cabinButtons)
    {
      requests.add(b.isPressed());
    }

    return requests;
  }

  public void setButtonLight(ButtonLight buttonLight, int floorNumber)
  {
    cabinButtons.get(floorNumber).setLight(buttonLight);
  }
}