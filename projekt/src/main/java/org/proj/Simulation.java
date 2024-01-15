package org.proj;

import org.proj.model.DayManager;
import org.proj.model.Genotype;
import org.proj.model.SimulationProps;
import org.proj.model.elements.Animal;
import org.proj.model.elements.Plant;
import org.proj.model.maps.AbstractWorldMap;
import org.proj.utils.CSVLogger;
import org.proj.utils.RandomPositionGenerator;
import org.proj.utils.Vector2d;

import java.util.*;

public class Simulation implements Runnable {
    private final AbstractWorldMap map;
    private final List<Animal> animals = new ArrayList<>();
    private boolean isRunning = true;
    private final SimulationProps simulationProps;
    private int accumulatedLifeSpan = 0;
    private int deadAnimals = 0;
    private final CSVLogger csvLogger;
    private final DayManager dayManager;
    private final Map<int[], Integer> genesCount = new HashMap<>();
    public List<Animal> getAnimals() {
        return animals;
    }

    public Simulation(AbstractWorldMap map, SimulationProps simulationProps) {
        map.setSimulation(this);
        this.map = map;
        this.simulationProps = simulationProps;
        csvLogger = new CSVLogger();
        dayManager = new DayManager(map, simulationProps, this);

        RandomPositionGenerator randomPositionGeneratorAnimals = new RandomPositionGenerator(simulationProps.getMapWidth(), simulationProps.getMapHeight(), simulationProps.getStartAnimalCount());
        for(Vector2d animalPosition : randomPositionGeneratorAnimals) {
            Animal animal = new Animal( animalPosition,
                                        simulationProps.getStartEnergy(),
                                        simulationProps.getMaxEnergy(),
                                        0,
                                        Genotype.getRandomGenes(simulationProps.getGenesCount()),
                                        simulationProps.getMoveStyle());
            animals.add(animal);
            int[] genome = animal.getGenome();
            genesCount.merge(genome, 1, Integer::sum);
            map.placeAnimals(animal.getPosition(), animal);
        }

        RandomPositionGenerator randomPositionGeneratorPlants = new RandomPositionGenerator(simulationProps.getMapWidth(), simulationProps.getMapHeight(), simulationProps.getStartPlantCount());
        for(Vector2d plantPosition : randomPositionGeneratorPlants) {
            map.placePlants(plantPosition, new Plant(plantPosition));
        }
    }

    @Override
    public void run() {
        while (!animals.isEmpty()) {
            if (isRunning) {
                synchronized (this) {
                    dayManager.Update();
                }

                if (simulationProps.shouldSaveCSV()) csvLogger.LogDay(simulationProps, map, this);
            }

            try {
                Thread.sleep(simulationProps.getSimulationStep());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void addAnimal(Animal animal){
        animals.add(animal);
        int[] genome = animal.getGenome();
        genesCount.merge(genome, 1, Integer::sum);
    }

    public Integer getDeadAnimalsCount() {
        return deadAnimals;
    }

    public synchronized void incrementDeadAnimalCount() {deadAnimals++;}

    public synchronized void addToAccumulatedLifeSpan(int value) {accumulatedLifeSpan += value;}

    public Map<int[], Integer> GetGenomeCount() { return genesCount; }

    public synchronized float getAverageEnergy() {
        float sum = 0;
        for (Animal animal : animals)
            sum += animal.getEnergy();

        return sum / animals.size();
    }

    public Integer getAliveAnimalsCount() {
        return animals.size();
    }

    public float getAverageLifeSpan() {
        return accumulatedLifeSpan / (float)deadAnimals;
    }

    public synchronized float getAverageChildrenCount() {
        float sum = 0;
        for (Animal animal : animals)
            sum += animal.getChildrenMade();
        return  sum / (animals.size());
    }

    public void togglePause() {
        isRunning = !isRunning;
    }

    public synchronized String getMostPopularGenotypeStr() {
        if (genesCount.isEmpty())  return "---";
        int[] genome = Collections.max(genesCount.entrySet(), Map.Entry.comparingByValue()).getKey();
        String genomeStr = "";
        for (int v : genome) {
            genomeStr += v;
        }
        return genomeStr;
    }

    public synchronized int[] getMostPopularGenotype() {
        if (genesCount.isEmpty()) return new int[1];
        return Collections.max(genesCount.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    public CSVLogger getCsvLogger() {
        return csvLogger;
    }
}
