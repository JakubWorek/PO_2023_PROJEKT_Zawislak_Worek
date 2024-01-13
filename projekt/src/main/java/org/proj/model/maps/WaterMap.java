package org.proj.model.maps;

import org.proj.model.SimulationProps;
import org.proj.model.elements.IWorldElement;
import org.proj.model.elements.Water;
import org.proj.utils.EMapDirection;
import org.proj.utils.PositionOrientationTuple;
import org.proj.utils.RandomPositionGenerator;
import org.proj.utils.Vector2d;

import java.util.*;

public class WaterMap extends AbstractWorldMap{
    private HashMap<Vector2d, Water> waters = new HashMap<>();
    public WaterMap(SimulationProps simulationProps){
        super(simulationProps);

        RandomPositionGenerator randomPositionGeneratorWater = new RandomPositionGenerator(simulationProps.getMapWidth(), simulationProps.getMapHeight(), width*height/10);
        for(Vector2d waterPosition : randomPositionGeneratorWater) {
            waters.put(waterPosition, new Water(waterPosition));
        }
    }

    public void makeWaterDoAnything(boolean isFlow){
        // wylosuj z obecnych wód 50%
        int waterCount = waters.size();
        int waterToChangeCount = waterCount/2;
        if (waterToChangeCount == 0) waterToChangeCount = 1;

        List<Vector2d> waterToChange = new ArrayList<>(waters.keySet());
        Collections.shuffle(waterToChange);


        for (int i = 0; i < waterToChangeCount && i < waterToChange.size(); i++) {
            Vector2d currentPosition = waterToChange.get(i);

            if (isFlow) {
                // Jeśli przypływ to wylosuj kierunek
                EMapDirection direction = EMapDirection.getRandomDirection();
                Vector2d newPosition = currentPosition.add(direction.unitVector());

                // Jeśli jest tam woda to zmień kierunek
                if (waters.containsKey(newPosition)) {
                    for (int j = 0; j < 8; j++) {
                        if (waters.containsKey(newPosition)) {
                            direction = direction.next();
                            newPosition = currentPosition.add(direction.unitVector());
                        }
                        else{
                            break;
                        }
                    }
                }

                // Jeśli nie ma tam zwierzaka
                if (!animals.containsKey(newPosition)) {
                    // Jeśli roślinka to usuń roślinkę i dodaj wodę
                    if (plants.containsKey(newPosition)) {
                        plants.remove(newPosition);
                        waters.put(newPosition, new Water(newPosition));
                    }
                    // Jeśli jest miejsce to dodaj wodę
                    else if (!waters.containsKey(newPosition)) {
                        waters.put(newPosition, new Water(newPosition));
                    }

                    if (newPosition.getX() <0 || newPosition.getX() > width-1 || newPosition.getY() < 0 || newPosition.getY() > height-1){
                        waters.remove(newPosition);
                    }

                }
            } else {
                // Jeśli odpływ, usuń wodę
                if (waters.size() > 1) {
                    waters.remove(currentPosition);
                }
            }
        }
    }

    public synchronized void calculateFreePositions(){
        freePositionsForPlants.clear();
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                Vector2d position = new Vector2d(x,y);
                if(!waters.containsKey(position)) freePositionsForPlants.add(position);
            }
        }
    }


    @Override
    public synchronized PositionOrientationTuple correctPosition(Vector2d oldPosition, Vector2d newPosition, EMapDirection orientation){
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
        // check if it is water on position
        if(waters.containsKey(new Vector2d(x, y))){
            return new PositionOrientationTuple(oldPosition, orientation);
        }

        return new PositionOrientationTuple(new Vector2d(x, y), orient);
    }

    @Override
    public synchronized IWorldElement objectAt(Vector2d position){
        if(animals.containsKey(position)) {
            if (animals.get(position).size() > 0)
                return animals.get(position).get(0);
        }
        if(waters.containsKey(position)) return waters.get(position);
        if(plants.containsKey(position)) return plants.get(position);
        return null;
    }

    @Override
    public Integer getEmptyCount() {
        Set<Vector2d> position = new HashSet<>();
        for (Vector2d pos : animals.keySet())
            if (!animals.get(pos).isEmpty())
                position.add(pos);
        position.addAll(waters.keySet());
        position.addAll(plants.keySet());
        return width*height - position.size();
    }
}
