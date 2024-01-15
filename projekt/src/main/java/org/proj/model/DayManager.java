package org.proj.model;

import org.proj.Simulation;
import org.proj.model.elements.Animal;
import org.proj.model.maps.AbstractWorldMap;
import org.proj.model.maps.EMapType;
import org.proj.model.maps.WaterMap;
import org.proj.utils.Vector2d;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DayManager {
    private final AbstractWorldMap map;
    private final SimulationProps simulationProps;
    private final Simulation simulation;

    public DayManager (AbstractWorldMap map_, SimulationProps simulationProps_, Simulation simulation_) {
        map = map_;
        simulationProps = simulationProps_;
        simulation = simulation_;
    }

    public void Update() {
        removeDeadAnimals();

        // if map is WaterMap, then expand/contract water and calculate free positions for plants
        updateWater();

        moveAnimals();
        eat();
        reproduce();

        incrementDayCounters();

        growPlants();

        map.mapChanged("Day elapsed");
    }

    private void incrementDayCounters() {
        simulationProps.incrementDaysElapsed();
        for(Animal animal : simulation.getAnimals()){
            animal.addAge();
        }
    }

    private void moveAnimals() {
        for(Animal animal : simulation.getAnimals()){
            map.move(animal);
            animal.removeEnergy(simulationProps.getMoveEnergy());
        }
    }

    private void updateWater() {
        if(map.getMapType() == EMapType.WATER && simulationProps.getDaysElapsed() % 2 == 0){
            ((WaterMap)map).makeWaterDoAnything(simulationProps.getDaysElapsed() % 10 != 0);
            ((WaterMap)map).calculateFreePositions();
        }
    }

    private void removeDeadAnimals() {
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
    }

    public void reproduce() {
        for (Vector2d position : map.getAnimals().keySet()) {
            List<Animal> animalList = map.getAnimals().get(position);
            if (animalList.size() > 1) {
                Animal a1 = animalList.get(0);
                Animal a2 = animalList.get(1);
                if (a1.getEnergy() > simulationProps.getEnergyLevelNeededToReproduce() && a2.getEnergy() > simulationProps.getEnergyLevelNeededToReproduce()) {
                    Animal child = new Animal(position, 2 * simulationProps.getEnergyLevelToPassToChild(),
                            simulationProps.getMaxEnergy(), simulationProps.getDaysElapsed(), Genotype.getGenesFromParents(a1, a2, simulationProps),
                            simulationProps.getMoveStyle());
                    synchronized (this) {
                        map.getAnimals().get(position).add(child);
                        a1.removeEnergy(simulationProps.getEnergyLevelToPassToChild());
                        a1.addChild();
                        a1.addChildToList(child);
                        a2.removeEnergy(simulationProps.getEnergyLevelToPassToChild());
                        a2.addChild();
                        a2.addChildToList(child);
                        simulation.addAnimal(child);
                    }
                }
            }
        }
    }

    public void eat() {
        Set<Vector2d> keys = new HashSet<>(map.getPlants().keySet());
        for ( Vector2d position : keys ){
            if ( map.getAnimals().containsKey(position) ) {
                List<Animal> animalList = map.getAnimals().get(position);
                if (!animalList.isEmpty()) {
                    Animal animal = animalList.get(0);
                    synchronized (this) {
                        animal.eat(simulationProps.getPlantEnergy());
                        map.getPlants().remove(position);
                        map.getFreePositionsForPlants().add(position);
                    }
                }
            }
        }
    }

    private void growPlants() {
        int plantsToAdd = simulationProps.getSpawnPlantPerDay();
        for (int i = 0; i<plantsToAdd; i++) {
            map.spawnPlant();
        }
    }
}
