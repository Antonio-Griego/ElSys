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
    private Set<FloorRequest> floorRequestsInUse;

    public RequestRouter(){

    }

    public void update(CabinStatus[] cabinStatuses, Set<FloorRequest> floorRequests, BuildingState buildingState){
        this.cabinStatuses = cabinStatuses;
        this.floorRequests = floorRequests;
        this.buildingState = buildingState;
    }

    public Integer[] getDestinations(){
      Integer [] destinations = new Integer[cabinStatuses.length];
      floorRequestsInUse = new HashSet<>();

        for(int i = 0; i < cabinStatuses.length; i++){
            CabinStatus cabinStatus = cabinStatuses[i];
            Integer bestDest = getNextCabinRequest(cabinStatus);
            bestDest = checkFloorRequests(cabinStatus, floorRequests, bestDest);
            //if no good dest stay at same floor
            if(bestDest == Integer.MAX_VALUE) bestDest = null;
            destinations[i] = bestDest;
        }
      
      return destinations;
    }

    private int getNextCabinRequest(CabinStatus cabinStatus){
        //TODO think about not using intmax
        int bestDest = Integer.MAX_VALUE;
        if(cabinStatus.getCabinRequests().isEmpty()) return bestDest;
        int currentFloor = cabinStatus.getFloor();
        CabinDirection dir = cabinStatus.getDirection();
        int oldDif = Math.abs(bestDest - currentFloor);
        List<Integer> possibleDests = cabinStatus.getCabinRequests().stream()
                .map(req -> req.getFloor())
                .collect(Collectors.toList());

        //look at cabin request in the current direction if possible
        if(dir == CabinDirection.DOWN) {
            possibleDests = possibleDests.stream()
                    .filter(floor -> floor < currentFloor)
                    .collect(Collectors.toList());
        }
        else if (dir == CabinDirection.UP) {
            possibleDests = possibleDests.stream()
                    .filter(floor -> floor > currentFloor)
                    .collect(Collectors.toList());
        }
        //if there are no cabinRequest in the current direction look at all requests
        if(possibleDests.isEmpty()){
            possibleDests = cabinStatus.getCabinRequests().stream()
                    .map(req -> req.getFloor())
                    .collect(Collectors.toList());
        }

        for (Integer dest: possibleDests) {
            int newDif = Math.abs(currentFloor - dest);
            if(newDif < oldDif){
                bestDest = dest;
            }
        }
        return bestDest;
    }

    private int checkFloorRequests(CabinStatus cabinStatus, Set<FloorRequest> floorRequests, int currDest){
        if(floorRequests.size() == 0) return currDest;
        int currentFloor = cabinStatus.getFloor();
        CabinDirection dir = cabinStatus.getDirection();
        int bestDest = currDest;
        FloorRequest bestRequest = null;
        int oldDif = Math.abs(bestDest - currentFloor);
        List<FloorRequest> possibleRequests;

        if(dir == CabinDirection.DOWN) {
            possibleRequests = floorRequests.stream()
                    .filter(fr -> fr.getDirection() == dir)
                    //.map(req -> req.getFloor())
                    .filter(floor -> floor.getFloor() < currentFloor)
                    .filter(req -> !floorRequestsInUse.contains(req))
                    .collect(Collectors.toList());
        }
        else if(dir == CabinDirection.UP) {
            possibleRequests = floorRequests.stream()
                    .filter(fr -> fr.getDirection() == dir)
                    //.map(req -> req.getFloor())
                    .filter(floor -> floor.getFloor() > currentFloor)
                    .filter(req -> !floorRequestsInUse.contains(req))
                    .collect(Collectors.toList());
        }
        else {
            possibleRequests = floorRequests.stream()
                    .filter(req -> !floorRequestsInUse.contains(req))
                    .collect(Collectors.toList());
        }

        for (FloorRequest dest: possibleRequests) {
            int newDif = Math.abs(currentFloor - dest.getFloor());
            if(newDif < oldDif){
                bestDest = dest.getFloor();
                bestRequest = dest;
            }
        }
        if(bestRequest != null) floorRequestsInUse.add(bestRequest);
        return bestDest;
    }
}
