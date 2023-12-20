package org.proj.model.maps;

import org.proj.model.Genotype;
import org.proj.model.SimulationProps;
import org.proj.model.elements.Animal;
import org.proj.model.elements.EMoveStyle;
import org.proj.model.elements.Plant;
import org.proj.utils.IMoveValidator;
import org.proj.utils.RandomPositionGenerator;
import org.proj.utils.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public abstract class AbstractWorldMap implements IMoveValidator {
    protected final HashMap<Vector2d, List<Animal>> animals;

    protected final HashMap<Vector2d, Plant> plants;

    protected int width;
    protected int height;

    //protected final List<MapChangeListener> listeners;
    //private final MapVisualizer mapVisualizer;

    protected String id;

    public AbstractWorldMap(int plantCount, int animalCount, int energy, int maxEnergy, EMoveStyle moveStyle) {
        animals = new HashMap<>();
        plants = new HashMap<>();

        RandomPositionGenerator randomPositionGeneratorPlants = new RandomPositionGenerator(width, height, plantCount);
        for(Vector2d plantPosition : randomPositionGeneratorPlants) {
            plants.put(plantPosition, new Plant(plantPosition));
        }

        RandomPositionGenerator randomPositionGeneratorAnimals = new RandomPositionGenerator(width, height, animalCount);
        for(Vector2d animalPosition : randomPositionGeneratorAnimals) {
            List<Animal> animalList = new ArrayList<>();
            animalList.add(new Animal(animalPosition, energy, maxEnergy, 0, Genotype.getRandomGenes(), moveStyle));
            animals.put(animalPosition, animalList);
        }

        //listeners = new ArrayList<>();
        //mapVisualizer = new MapVisualizer(this);
    }
    /*
    public void addListener(MapChangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(MapChangeListener listener) {
        listeners.remove(listener);
    }

    public void mapChanged(String msg) {
        for (MapChangeListener listener : listeners) {
            listener.mapChanged(this, msg);
        }
    }
    */

    public void move(Animal animal)  {
        animals.get(animal.getPosition()).remove(animal);
        animal.move(this);
        animals.get(animal.getPosition()).add(animal);

        // reproduction
        int numOfAnimals = animals.get(animal.getPosition()).size();
        if (numOfAnimals >= 2) {
            for (int i = 0; i < numOfAnimals; i+=2) {
                Animal a1 = animals.get(animal.getPosition()).get(i);
                Animal a2 = animals.get(animal.getPosition()).get(i+1);
                if (a1.getEnergy() >= SimulationProps.getEnergyLevelNeededToReproduce()
                        && a2.getEnergy() >= SimulationProps.getEnergyLevelNeededToReproduce()) {

                    animals.get(animal.getPosition()).add(new Animal(animal.getPosition(), 2*SimulationProps.getEnergyLevelToPassToChild(),
                            SimulationProps.getMaxEnergy(), SimulationProps.getDaysElapsed(), Genotype.getGenesFromParents(a1, a2),
                            SimulationProps.getMoveStyle()));
                    a1.removeEnergy(SimulationProps.getEnergyLevelToPassToChild());
                    a2.removeEnergy(SimulationProps.getEnergyLevelToPassToChild());
                }
            }
        }

        //mapChanged("Animal moved from " + from + " to "  + animal.getPosition());
    }

    @Override
    public String toString() {
        return "";
    }

    public String getId() {
        return id;
    }
}