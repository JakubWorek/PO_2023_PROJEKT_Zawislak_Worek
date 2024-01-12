package org.proj;

import org.proj.model.Genotype;
import org.proj.model.SimulationProps;
import org.proj.model.elements.Animal;
import org.proj.model.elements.Plant;
import org.proj.model.maps.AbstractWorldMap;
import org.proj.model.maps.WaterMap;
import org.proj.utils.RandomPositionGenerator;
import org.proj.utils.Vector2d;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Simulation implements Runnable {
    private final AbstractWorldMap map;
    private List<Animal> animals = new ArrayList<>();
    private boolean isRunning = true;
    private SimulationProps simulationProps;

    private int accumulatedLifeSpan = 0;

    private int deadAnimals = 0;

    private String csvContent = "Day;Animals count;Plants count;Free fields count;Most popular genotype;Average energy level;Average lifespan;Average children count of living animals\n";

    public List<Animal> getAnimals() {
        return animals;
    }

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
            if (!isRunning) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            // remove dead animals
            Set<Animal> animalsToRemove = new HashSet<>(animals);
            for(Animal animal : animalsToRemove){
                if(animal.getEnergy()<=0){
                    animal.setDeathDate(simulationProps.getDaysElapsed());
                    deadAnimals += 1;
                    accumulatedLifeSpan += animal.getAge();
                    map.removeAnimal(animal);
                    animals.remove(animal);
                }
            }
            // if map is WaterMap, then expand/contract water and calculate free positions for plants
            if(map.getClass().getSimpleName().equals("WaterMap")){
                ((WaterMap)map).makeWaterDoAnything();
                ((WaterMap)map).calculateFreePositions();
            }
            // move animals
            for(Animal animal : animals){
                map.move(animal);
            }
            // decrease energy after move
            for(Animal animal : animals){
                animal.removeEnergy(simulationProps.getMoveEnergy());
            }
            // eat
            map.eat();
            // reproduce animals
            map.reproduce();
            // save stats to csv *
            if (simulationProps.shouldSaveCSV()) {
                csvContent += simulationProps.getDaysElapsed() + ";" + map.getAliveAnimalsCount() + ";" + map.getPlantsCount() + ";" +map.getEmptyCount() + ";" + "" + ";" + getAvarageEnergy() + ";" + getAverageLifeSpan() + ";" + getAverageChildrenCount() + "\n";
            }

            // add day
            simulationProps.incrementDaysElapsed();
            // add age
            for(Animal animal : animals){
                animal.addAge();
            }
            // grow new plants
            map.growPlants();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addAnimal(Animal animal){
        animals.add(animal);
    }

    public Integer getDeadAnimalsCount() {
        return deadAnimals;
    }

    public float getAvarageEnergy() {
        float sum = 0;
        for (Animal animal : animals)
            sum += animal.getEnergy();

        return sum / animals.size();
    }


    public float getAverageLifeSpan() {
        return accumulatedLifeSpan / (float)deadAnimals;
    }

    public float getAverageChildrenCount() {
        float sum = 0;
        for (Animal animal : animals)
            sum += animal.getChildrenMade();
        return  sum / (animals.size());
    }

    public void togglePause() {
        isRunning = !isRunning;
    }
}
