package org.proj;

import org.proj.model.Genotype;
import org.proj.model.SimulationProps;
import org.proj.model.elements.Animal;
import org.proj.model.elements.Plant;
import org.proj.model.maps.AbstractWorldMap;
import org.proj.utils.RandomPositionGenerator;
import org.proj.utils.Vector2d;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Simulation implements Runnable {
    private final AbstractWorldMap map;
    private List<Animal> animals = new ArrayList<>();
    private boolean isRunning = true;
    private SimulationProps simulationProps;

    public Simulation(AbstractWorldMap map, SimulationProps simulationProps) {
        map.setSimulation(this);
        this.map = map;
        this.simulationProps = simulationProps;

        RandomPositionGenerator randomPositionGeneratorAnimals = new RandomPositionGenerator(simulationProps.getMapWidth(), simulationProps.getMapHeight(), simulationProps.getStartAnimalCount());
        for(Vector2d animalPosition : randomPositionGeneratorAnimals) {
            Animal animal = new Animal( animalPosition,
                                        simulationProps.getStartEnergy(),
                                        simulationProps.getMaxEnergy(),
                                        0,
                                        Genotype.getRandomGenes(simulationProps.getGenesCount()),
                                        simulationProps.getMoveStyle());
            animals.add(animal);
            map.placeAnimals(animal.getPosition(), animal);
        }

        RandomPositionGenerator randomPositionGeneratorPlants = new RandomPositionGenerator(simulationProps.getMapWidth(), simulationProps.getMapHeight(), simulationProps.getStartPlantCount());
        for(Vector2d plantPosition : randomPositionGeneratorPlants) {
            map.placePlants(plantPosition, new Plant(plantPosition));
        }
    }

    @Override
    public void run(){
        while(animals.size()>0){
            // add day
            simulationProps.incrementDaysElapsed();
            // move animals
            for(Animal animal : animals){
                //animal.move(map);
                map.move(animal);
            }
            // decrease energy after move
            for(Animal animal : animals){
                animal.removeEnergy(10);
            }
            // eat
            map.eat();
            // reproduce animals
            map.reproduce();
            // remove dead animals
            Set<Animal> animalsToRemove = new HashSet<>(animals);
            for(Animal animal : animalsToRemove){
                if(animal.getEnergy()<=0){
                    map.removeAnimal(animal);
                    animals.remove(animal);
                }
            }
            //System.out.println("X");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addAnimal(Animal animal){
        animals.add(animal);
    }
}
