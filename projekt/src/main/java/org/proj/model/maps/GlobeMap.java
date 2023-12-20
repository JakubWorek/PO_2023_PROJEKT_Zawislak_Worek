package org.proj.model.maps;

import org.proj.model.elements.EMoveStyle;
import org.proj.utils.EMapDirection;
import org.proj.utils.PositionOrientationTuple;
import org.proj.utils.Vector2d;

public class GlobeMap extends AbstractWorldMap {

    public GlobeMap(int plantCount, int animalCount, int energy, int maxEnergy, EMoveStyle moveStyle) {
        super(plantCount, animalCount, energy, maxEnergy, moveStyle);
    }

    @Override
    public PositionOrientationTuple correctPosition(Vector2d position, EMapDirection orientation) {
        int x = (position.getX()+width)%width;
        int y = position.getY();
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
