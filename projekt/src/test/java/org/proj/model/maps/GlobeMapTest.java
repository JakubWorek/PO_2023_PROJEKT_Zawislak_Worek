package org.proj.model.maps;

import org.junit.jupiter.api.Test;
import org.proj.Simulation;
import org.proj.model.SimulationProps;
import org.proj.model.elements.Animal;
import org.proj.model.elements.EMoveStyle;
import org.proj.model.elements.EMutationStyle;
import org.proj.model.elements.Plant;
import org.proj.utils.Vector2d;
import org.proj.utils.EMapDirection;
import org.proj.utils.PositionOrientationTuple;
import static org.junit.jupiter.api.Assertions.*;

public class GlobeMapTest {
    @Test
    public void testCorrectPosition(){
        GlobeMap globeMap = new GlobeMap(new SimulationProps(5, 5, 2, 0, 0, 0, 100, 0, EMoveStyle.FULLY_PREDESTINED, EMutationStyle.FULLY_RANDOM, EMapType.WATER, 6, 40, 20, 0, false, "csv.csv", 1, 0, 0));
        assertEquals(new PositionOrientationTuple(new Vector2d(0, 1), EMapDirection.NORTH), globeMap.correctPosition(new Vector2d(0, 0), new Vector2d(0, -1), EMapDirection.SOUTH));
        assertEquals(new PositionOrientationTuple(new Vector2d(0, 3), EMapDirection.SOUTH), globeMap.correctPosition(new Vector2d(0, 4), new Vector2d(0, 5), EMapDirection.NORTH));
        assertEquals(new PositionOrientationTuple(new Vector2d(1, 1), EMapDirection.NORTH_WEST), globeMap.correctPosition(new Vector2d(0, 0), new Vector2d(1, -1), EMapDirection.SOUTH_WEST));
        assertEquals(new PositionOrientationTuple(new Vector2d(1, 3), EMapDirection.SOUTH_WEST), globeMap.correctPosition(new Vector2d(0, 4), new Vector2d(1, 5), EMapDirection.NORTH_WEST));
        assertEquals(new PositionOrientationTuple(new Vector2d(4, 1), EMapDirection.NORTH_EAST), globeMap.correctPosition(new Vector2d(0, 0), new Vector2d(4, -1), EMapDirection.SOUTH_EAST));
        assertEquals(new PositionOrientationTuple(new Vector2d(4, 3), EMapDirection.SOUTH_EAST), globeMap.correctPosition(new Vector2d(0, 4), new Vector2d(4, 5), EMapDirection.NORTH_EAST));
        assertEquals(new PositionOrientationTuple(new Vector2d(0, 1), EMapDirection.NORTH), globeMap.correctPosition(new Vector2d(0, 0), new Vector2d(5, -1), EMapDirection.SOUTH));
        assertEquals(new PositionOrientationTuple(new Vector2d(0, 3), EMapDirection.SOUTH), globeMap.correctPosition(new Vector2d(0, 4), new Vector2d(5, 5), EMapDirection.NORTH));
        assertEquals(new PositionOrientationTuple(new Vector2d(1, 1), EMapDirection.NORTH_WEST), globeMap.correctPosition(new Vector2d(0, 0), new Vector2d(6, -1), EMapDirection.SOUTH_WEST));
        assertEquals(new PositionOrientationTuple(new Vector2d(1, 3), EMapDirection.SOUTH_WEST), globeMap.correctPosition(new Vector2d(0, 4), new Vector2d(6, 5), EMapDirection.NORTH_WEST));
        assertEquals(new PositionOrientationTuple(new Vector2d(4, 1), EMapDirection.NORTH_EAST), globeMap.correctPosition(new Vector2d(0, 0), new Vector2d(9, -1), EMapDirection.SOUTH_EAST));
        assertEquals(new PositionOrientationTuple(new Vector2d(4, 3), EMapDirection.SOUTH_EAST), globeMap.correctPosition(new Vector2d(0, 4), new Vector2d(9, 5), EMapDirection.NORTH_EAST));
        assertEquals(new PositionOrientationTuple(new Vector2d(4, 1), EMapDirection.NORTH), globeMap.correctPosition(new Vector2d(0, 0), new Vector2d(-1, -1), EMapDirection.SOUTH));
        assertEquals(new PositionOrientationTuple(new Vector2d(4, 3), EMapDirection.SOUTH), globeMap.correctPosition(new Vector2d(0, 4), new Vector2d(-1, 5), EMapDirection.NORTH));
        assertEquals(new PositionOrientationTuple(new Vector2d(4, 1), EMapDirection.NORTH_WEST), globeMap.correctPosition(new Vector2d(0, 0), new Vector2d(-1, -1), EMapDirection.SOUTH_WEST));
    }

    @Test
    public void testEat(){
        SimulationProps simulationProps = new SimulationProps(5, 5, 1, 0, 0, 0, 100, 10, EMoveStyle.FULLY_PREDESTINED, EMutationStyle.FULLY_RANDOM, EMapType.WATER, 6, 40, 20, 0, false, "csv.csv", 1, 0, 0);
        GlobeMap globeMap = new GlobeMap(simulationProps);
        Simulation simulation = new Simulation(globeMap, simulationProps);
        globeMap.setSimulation(simulation);
        globeMap.placeAnimals(new Vector2d(0, 0), new Animal(new Vector2d(0, 0), simulationProps));
        globeMap.placePlants(new Vector2d(0, 0), new Plant(new Vector2d(0, 0)));
        simulation.getDayManager().eat();
        assertEquals(110, globeMap.getAnimals().get(new Vector2d(0, 0)).get(0).getEnergy());
    }

    @Test
    public void testEatWith2Animals(){
        SimulationProps simulationProps = new SimulationProps(5, 5, 1, 0, 0, 0, 100, 10, EMoveStyle.FULLY_PREDESTINED, EMutationStyle.FULLY_RANDOM, EMapType.WATER, 6, 40, 20, 0, false, "csv.csv", 1, 0, 0);
        GlobeMap globeMap = new GlobeMap(simulationProps);
        Simulation simulation = new Simulation(globeMap, simulationProps);
        globeMap.setSimulation(simulation);
        globeMap.placeAnimals(new Vector2d(0, 0), new Animal(new Vector2d(0, 0), simulationProps));
        globeMap.placeAnimals(new Vector2d(0, 0), new Animal(new Vector2d(0, 0), simulationProps));
        globeMap.placePlants(new Vector2d(0, 0), new Plant(new Vector2d(0, 0)));
        simulation.getDayManager().eat();
        assertEquals(110, globeMap.getAnimals().get(new Vector2d(0, 0)).get(0).getEnergy());
        assertEquals(100, globeMap.getAnimals().get(new Vector2d(0, 0)).get(1).getEnergy());
    }

    @Test
    public void testReproduce(){
        SimulationProps simulationProps = new SimulationProps(5, 5, 1, 0, 0, 0, 100, 10, EMoveStyle.FULLY_PREDESTINED, EMutationStyle.FULLY_RANDOM, EMapType.WATER, 6, 40, 20, 0, false, "csv.csv", 1, 0, 0);
        GlobeMap globeMap = new GlobeMap(simulationProps);
        Simulation simulation = new Simulation(globeMap, simulationProps);
        globeMap.setSimulation(simulation);
        globeMap.placeAnimals(new Vector2d(0, 0), new Animal(new Vector2d(0, 0), simulationProps));
        globeMap.placeAnimals(new Vector2d(0, 0), new Animal(new Vector2d(0, 0), simulationProps));
        globeMap.placePlants(new Vector2d(0, 0), new Plant(new Vector2d(0, 0)));
        simulationProps.incrementDaysElapsed();
        simulation.getDayManager().reproduce();
        assertEquals(80, globeMap.getAnimals().get(new Vector2d(0, 0)).get(0).getEnergy());
        assertEquals(80, globeMap.getAnimals().get(new Vector2d(0, 0)).get(1).getEnergy());
        assertEquals(3, globeMap.getAnimals().get(new Vector2d(0, 0)).size());
        assertEquals(40, globeMap.getAnimals().get(new Vector2d(0, 0)).get(2).getEnergy());
    }

    @Test
    public void testAnimalMove(){
        SimulationProps simulationProps = new SimulationProps(5, 5, 1, 0, 0, 0, 100, 10, EMoveStyle.FULLY_PREDESTINED, EMutationStyle.FULLY_RANDOM, EMapType.WATER, 6, 40, 20, 0, false, "csv.csv", 1, 0, 0);
        GlobeMap globeMap = new GlobeMap(simulationProps);
        Animal animal = new Animal(new Vector2d(0, 0), simulationProps);
        int [] genome = {0, 0, 0, 0, 0, 0};
        animal.setGenome(genome);
        animal.setOrientation(EMapDirection.NORTH);
        globeMap.placeAnimals(new Vector2d(0, 0), animal);
        animal.move(globeMap);
        assertEquals(new Vector2d(0, 1), animal.getPosition());
        assertEquals(EMapDirection.NORTH, animal.getOrientation());
    }
}
