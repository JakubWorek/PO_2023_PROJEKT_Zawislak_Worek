package org.proj.model.maps;

import org.proj.Simulation;
import org.proj.model.Genotype;
import org.proj.model.SimulationProps;
import org.proj.model.elements.Animal;
import org.proj.model.elements.EMoveStyle;
import org.proj.model.elements.IWorldElement;
import org.proj.model.elements.Plant;
import org.proj.presenter.IMapChangeListener;
import org.proj.utils.IMoveValidator;
import org.proj.utils.RandomPositionGenerator;
import org.proj.utils.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public abstract class AbstractWorldMap implements IMoveValidator {
    protected HashMap<Vector2d, List<Animal>> animals;

    protected HashMap<Vector2d, Plant> plants;

    protected int width;
    protected int height;

    protected final List<IMapChangeListener> listeners;
    //private final MapVisualizer mapVisualizer;

    protected String id;
    protected Simulation simulation;
    SimulationProps simulationProps;

    public AbstractWorldMap(SimulationProps simulationProps) {
        animals = new HashMap<>();
        plants = new HashMap<>();
        listeners = new LinkedList<>();

        width = simulationProps.getMapWidth();
        height = simulationProps.getMapHeight();

        this.simulationProps = simulationProps;
        //mapVisualizer = new MapVisualizer(this);
    }

    public void placeAnimals(Vector2d animalPosition, Animal animal){
        if (animals.containsKey(animalPosition)) {
            animals.get(animalPosition).add(animal);
        }
        else {
            List<Animal> animalList = new ArrayList<>();
            animalList.add(animal);
            animals.put(animalPosition, animalList);
        }
    }

    public void placePlants(Vector2d plantPosition, Plant plant) {
        plants.put(plantPosition, plant);
    }

    public void addListener(IMapChangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(IMapChangeListener listener) {
        listeners.remove(listener);
    }

    public void mapChanged(String msg) {
        for (IMapChangeListener listener : listeners) {
            listener.mapChanged(this, msg);
        }
    }


    public void move(Animal animal)  {
        animals.get(animal.getPosition()).remove(animal);
        animal.move(this);
        placeAnimals(animal.getPosition(), animal);
        mapChanged("Animal moved to "  + animal.getPosition());
    }

    @Override
    public String toString() {
        return "";
    }

    public String getId() {
        return id;
    }
}