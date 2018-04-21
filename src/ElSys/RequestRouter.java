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
    private Set<FloorRequest> floorRequests;
    private BuildingState buildingState;

    public RequestRouter(){

    }

    public void update(CabinStatus[] cabinStatuses, Set<FloorRequest> floorRequests, BuildingState buildingState){
        this.cabinStatuses = cabinStatuses;
        this.floorRequests = floorRequests;
        this.buildingState = buildingState;
    }

    public Integer[] getDestinations(){
      Integer [] destinations = new Integer[cabinStatuses.length];
      
      Set<FloorRequest> validRequests;
      Set<FloorRequest> invalidRequests;
      
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
      }
      
      return destinations;
    }
    
    private Set<FloorRequest> getValidRequests(CabinStatus cabinStatus)
    {
      Set<FloorRequest> validRequests = new HashSet<>(cabinStatus.getCabinRequests());
      validRequests.addAll(floorRequests);
      if(cabinStatus.getDirection() == CabinDirection.UP)
      {
        return validRequests.stream()
            .filter(r -> r.getFloor() >= cabinStatus.getFloor())
            .collect(Collectors.toSet());
      }
      
      else if(cabinStatus.getDirection() == CabinDirection.DOWN)
      {
        return validRequests.stream()
            .filter(r -> r.getFloor() <= cabinStatus.getFloor())
            .collect(Collectors.toSet());
      }
      
      else
      {
        return validRequests;
      }
    }
}
