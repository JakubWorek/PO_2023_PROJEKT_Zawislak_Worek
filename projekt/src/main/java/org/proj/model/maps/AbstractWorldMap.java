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

import java.util.*;

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

    public void reproduce() {
        for (Vector2d position : animals.keySet()) {
            List<Animal> animalList = animals.get(position);
            if (animalList.size() > 1) {
                Animal a1 = animalList.get(0);
                Animal a2 = animalList.get(1);
                for (Animal animal : animalList) {
                    if (animal == a1 || animal == a2) continue;
                    Animal a3 = a1;
                    a1 = a1.compareWith(animal);
                    if (a3 != a1) {
                        a2 = a3;
                    } else {
                        a2 = a2.compareWith(animal);
                    }
                }
                if (a1.getEnergy() > simulationProps.getEnergyLevelNeededToReproduce() && a2.getEnergy() > simulationProps.getEnergyLevelNeededToReproduce()) {
                    Animal child = new Animal(position, 2 * simulationProps.getEnergyLevelToPassToChild(),
                            simulationProps.getMaxEnergy(), simulationProps.getDaysElapsed(), Genotype.getGenesFromParents(a1, a2, simulationProps.getMutationStyle(), simulationProps.getGenesCount()),
                            simulationProps.getMoveStyle());
                    animals.get(position).add(child);
                    a1.removeEnergy(simulationProps.getEnergyLevelToPassToChild());
                    a1.addChild();
                    a2.removeEnergy(simulationProps.getEnergyLevelToPassToChild());
                    a2.addChild();
                    simulation.addAnimal(child);
                }
            }
        }
    }

    public void eat() {
        for ( Vector2d position : plants.keySet() ){
            if ( animals.containsKey(position) ){
                List<Animal> animalList = animals.get(position);
                if (animalList.size() > 0) {
                    Animal animal = animalList.get(0);
                    //for (Animal animal1 : animalList) {
                    //    animal = animal.compareWith(animal1);
                    //}
                    animal.eat(simulationProps.getPlantEnergy());
                    //plants.remove(position);
                }
            }

        }
    }

    public IWorldElement objectAt(Vector2d position){
        if(animals.containsKey(position)) {
            if (animals.get(position).size() > 0)
                return animals.get(position).get(0);
        }
        if(plants.containsKey(position)) return plants.get(position);
        return null;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public String toString() {
        return "";
    }

    public String getId() {
        return id;
    }
}