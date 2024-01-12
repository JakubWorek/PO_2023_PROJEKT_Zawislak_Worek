package org.proj;

import org.proj.model.Genotype;
import org.proj.model.SimulationProps;
import org.proj.model.elements.Animal;
import org.proj.model.elements.Plant;
import org.proj.model.maps.AbstractWorldMap;
import org.proj.model.maps.WaterMap;
import org.proj.utils.RandomPositionGenerator;
import org.proj.utils.Vector2d;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class Simulation implements Runnable {
    private final AbstractWorldMap map;
    private List<Animal> animals = new ArrayList<>();
    private boolean isRunning = true;
    private SimulationProps simulationProps;

    private int accumulatedLifeSpan = 0;

    private int deadAnimals = 0;

    private String csvContent = "Day;Animals count;Plants count;Free fields count;Most popular genotype;Average energy level;Average lifespan;Average children count of living animals\n";

    private Map genesCount = new HashMap<int[], Integer>();

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
            int[] genome = animal.getGenome();
            if (genesCount.get(genome) == null) genesCount.put(genome, 1);
            else genesCount.put(genome, (Integer)genesCount.get(genome)+1);
            map.placeAnimals(animal.getPosition(), animal);
        }

        RandomPositionGenerator randomPositionGeneratorPlants = new RandomPositionGenerator(simulationProps.getMapWidth(), simulationProps.getMapHeight(), simulationProps.getStartPlantCount());
        for(Vector2d plantPosition : randomPositionGeneratorPlants) {
            map.placePlants(plantPosition, new Plant(plantPosition));
        }
    }

    public void saveToCSV() {
        File file = new File(simulationProps.getCSVName());
        try {
            FileWriter fr = new FileWriter(file, false);
            fr.write(csvContent);
            fr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(){
        while(animals.size()>0){
            if (!isRunning) {
                try {
                    Thread.sleep(100);
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
                    genesCount.put(animal.getGenome(), (Integer)genesCount.get(animal.getGenome())-1);
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
                String avgLifespan = "---";
                if (deadAnimals > 0) avgLifespan = String.valueOf(getAverageLifeSpan());
                csvContent += simulationProps.getDaysElapsed() + ";" + map.getAliveAnimalsCount() + ";" + map.getPlantsCount() + ";" + map.getEmptyCount() + ";" + getMostPopularGenotypeStr() + ";" + getAvarageEnergy() + ";" + avgLifespan + ";" + getAverageChildrenCount() + "\n";
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
        int[] genome = animal.getGenome();
        if (genesCount.get(genome) == null) genesCount.put(genome, 1);
        else genesCount.put(genome, (Integer)genesCount.get(genome)+1);
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

    public String getMostPopularGenotypeStr() {
        if (genesCount.size() == 0)  return "---";
        int[] genome = (int[])Collections.max(genesCount.entrySet(), Map.Entry.comparingByValue()).getKey();
        String genomeStr = "";
        for (int v : genome) {
            genomeStr += v;
        }
        return genomeStr;
    }
}
