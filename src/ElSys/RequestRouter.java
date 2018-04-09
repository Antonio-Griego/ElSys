package ElSys;

import ElSys.Enums.BuildingState;
import ElSys.Enums.CabinDirection;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Set;

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
        Set<FloorRequest> cabinRequests;
        for(int i = 0; i < cabinStatuses.length; i++){
          cabinRequests = cabinStatuses[i].getCabinRequests();
          destinations[i] = null;
          for(FloorRequest r : cabinRequests)
          {
            if(cabinStatuses[i].getDirection() == CabinDirection.UP)
            {
              if(destinations[i] == null) destinations[i] = r.getFloor();
              
              else
              {
                if(cabinStatuses[i].getDirection() == CabinDirection.UP)
                {
                  if(r.getFloor() - cabinStatuses[i].getFloor() > 0 )
                }
              }
            }
          }
          
          //if(!floorRequests.isEmpty()) destinations[i] = floorRequests.poll().getFloor();
        }
        return destinations;
    }
}
