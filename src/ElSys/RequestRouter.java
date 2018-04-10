package ElSys;

import ElSys.Enums.BuildingState;
import ElSys.Enums.CabinDirection;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by caleb on 4/8/2018.
 */
public class RequestRouter {

    private CabinStatus[] cabinStatuses;
    private Queue<FloorRequest> floorRequests;
    private BuildingState buildingState;

    public RequestRouter(){

    }

    public void update(CabinStatus[] cabinStatuses, Queue<FloorRequest> floorRequests, BuildingState buildingState){
        this.cabinStatuses = cabinStatuses;
        this.floorRequests = floorRequests;
        this.buildingState = buildingState;
    }

    public Integer[] getDestinations(){
      //TODO make this not suck
      Integer [] destinations = new Integer[cabinStatuses.length];
      
      Set<FloorRequest> validRequests;
      
      for(int i = 0; i < cabinStatuses.length; i++){
        validRequests = getValidRequests(cabinStatuses[i]);
        destinations[i] = null;
        for(FloorRequest r : validRequests)
        {
          if(destinations[i] == null) destinations[i] = r.getFloor();
          
          else
          {
            if(Math.abs(cabinStatuses[i].getFloor() - r.getFloor()) < Math.abs(cabinStatuses[i].getFloor() - destinations[i]))
            {
              destinations[i] = r.getFloor();
            }
          }
        }
          
        //if(!floorRequests.isEmpty()) destinations[i] = floorRequests.poll().getFloor();
      }
      
      return destinations;
    }
    
    private Set<FloorRequest> getValidRequests(CabinStatus cabinStatus)
    {
      if(cabinStatus.getDirection() == CabinDirection.UP)
      {
        return cabinStatus.getCabinRequests().stream()
            .filter(r -> r.getFloor() >= cabinStatus.getFloor())
            .collect(Collectors.toSet());
      }
      
      else if(cabinStatus.getDirection() == CabinDirection.DOWN)
      {
        return cabinStatus.getCabinRequests().stream()
            .filter(r -> r.getFloor() <= cabinStatus.getFloor())
            .collect(Collectors.toSet());
      }
      
      else
      {
        return cabinStatus.getCabinRequests();
      }
    }
}
