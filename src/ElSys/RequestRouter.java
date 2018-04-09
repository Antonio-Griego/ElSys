package ElSys;

import ElSys.Enums.BuildingState;

import java.util.ArrayList;
import java.util.Queue;

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

    public int[] getDestinations(){
        //TODO make this not suck
        int[] destinations = new int[4];
        for(int i = 0; i < cabinStatuses.length; i++){
            destinations[i] = floorRequests.poll().getFloor();
        }
        return destinations;
    }
}
