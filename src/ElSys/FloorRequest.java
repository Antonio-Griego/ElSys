package ElSys;

import ElSys.Enums.CabinDirection;

public class FloorRequest
{
    private final int floor;
    private final CabinDirection cabinDirection;

    FloorRequest(final int floor, final CabinDirection cabinDirection)
    {
        this.floor = floor;
        this.cabinDirection = cabinDirection;
    }

    public int getFloor()
    {
        return floor;
    }

    public CabinDirection getDirection()
    {
        return cabinDirection;
    }
}
