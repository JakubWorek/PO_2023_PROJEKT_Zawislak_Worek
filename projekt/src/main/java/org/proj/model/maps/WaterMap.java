package org.proj.model.maps;

import org.proj.model.SimulationProps;
import org.proj.model.elements.Water;
import org.proj.utils.EMapDirection;
import org.proj.utils.PositionOrientationTuple;
import org.proj.utils.Vector2d;

import java.util.HashMap;

public class WaterMap extends AbstractWorldMap{
    private HashMap<Vector2d, Water> waters;
    public WaterMap(SimulationProps simulationProps){
        super(simulationProps);
    }

    public void doShitWithWater(){
        // wylosuj z obecnych wód 10%
            // wylosuj czy jest przypływ czy odpływ
                // jeśli przypływ to wylosuj kierunek
                    // jeśli jest miejsce to dodaj wodę
                    // jeśli zwierzak to wylosuj nowy kierunek
                    // jeśli roślinka to usuń roślinkę i dodaj wodę
                // jeśli odpływ usuń wodę
    }

    @Override
    public PositionOrientationTuple correctPosition(Vector2d oldPosition, Vector2d newPosition, EMapDirection orientation){
        // check if it is water on position
        if(waters.containsKey(newPosition)){
            return new PositionOrientationTuple(oldPosition, orientation.rotate(4));
        }
        int x = (newPosition.getX()+width)%width;
        int y = newPosition.getY();
        EMapDirection orient = orientation;
        if (y < 0) {
            y = 1;
            if (orientation == EMapDirection.SOUTH) orient = EMapDirection.NORTH;
            else if (orientation == EMapDirection.SOUTH_EAST) orient = EMapDirection.NORTH_EAST;
            else if (orientation == EMapDirection.SOUTH_WEST) orient = EMapDirection.NORTH_WEST;
        }
        if (y >= height) {
            y = height-2;
            if (orientation == EMapDirection.NORTH) orient = EMapDirection.SOUTH;
            else if (orientation == EMapDirection.NORTH_EAST) orient = EMapDirection.SOUTH_EAST;
            else if (orientation == EMapDirection.NORTH_WEST) orient = EMapDirection.SOUTH_WEST;
        }

        return new PositionOrientationTuple(new Vector2d(x, y), orient);
    }
}
