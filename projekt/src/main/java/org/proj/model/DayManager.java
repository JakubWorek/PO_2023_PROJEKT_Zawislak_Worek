package org.proj.model;

import org.proj.Simulation;
import org.proj.model.elements.Animal;
import org.proj.model.maps.AbstractWorldMap;
import org.proj.model.maps.WaterMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DayManager {
    private AbstractWorldMap map;
    private SimulationProps simulationProps;
    private Simulation simulation;

    public DayManager (AbstractWorldMap map_, SimulationProps simulationProps_, Simulation simulation_) {
        map = map_;
        simulationProps = simulationProps_;
        simulation = simulation_;
    }

    public void Update() {
        // remove dead animals
        Set<Animal> animalsToRemove = new HashSet<>(simulation.getAnimals());
        for(Animal animal : animalsToRemove){
            if(animal.getEnergy()<=0){
                animal.setDeathDate(simulationProps.getDaysElapsed());
                simulation.GetGenomeCount().put(animal.getGenome(), (Integer) simulation.GetGenomeCount().get(animal.getGenome()) - 1);
                simulation.incrementDeadAnimalCount();
                simulation.addToAccumulatedLifeSpan(animal.getAge());
                map.removeAnimal(animal);
                simulation.getAnimals().remove(animal);
            }
        }
        // if map is WaterMap, then expand/contract water and calculate free positions for plants
        if(map.getClass().getSimpleName().equals("WaterMap") && simulationProps.getDaysElapsed() % 2 == 0){
            ((WaterMap)map).makeWaterDoAnything(simulationProps.getDaysElapsed() % 10 != 0);
            ((WaterMap)map).calculateFreePositions();
        }
        // move animals and decrease energy
        for(Animal animal : simulation.getAnimals()){
            map.move(animal);
            animal.removeEnergy(simulationProps.getMoveEnergy());
        }
        // eat
        map.eat();
        // reproduce animals
        map.reproduce();

        // add day
        simulationProps.incrementDaysElapsed();
        // add age
        for(Animal animal : simulation.getAnimals()){
            animal.addAge();
        }
        // grow new plants
        map.growPlants();

        map.mapChanged("Day elapsed");
    }
}
