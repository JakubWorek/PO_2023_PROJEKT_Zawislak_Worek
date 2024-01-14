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
    protected static final Random random = new Random();
    protected HashMap<Vector2d, List<Animal>> animals;
    protected HashMap<Vector2d, Plant> plants;
    protected List<Vector2d> freePositionsForPlants = new ArrayList<>();
    protected int width;
    protected int height;
    private final ForestedEquator forestedEquator;
    protected final List<IMapChangeListener> listeners;

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

        forestedEquator = new ForestedEquator(this.simulationProps.getEquatorHeight(), width, height);

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                Vector2d position = new Vector2d(x,y);
                freePositionsForPlants.add(position);
            }
        }
    }

    public void placeAnimals(Vector2d animalPosition, Animal animal){
        if (animals.containsKey(animalPosition)) {
            synchronized (this) {
                animals.get(animalPosition).add(animal);
                animals.get(animalPosition).sort(Comparator.comparing(Animal::getEnergy));
            }
        }
        else {
            List<Animal> animalList = new ArrayList<>();
            animalList.add(animal);
            synchronized (this) {
                animals.put(animalPosition, animalList);
            }
        }
    }

    public synchronized void removeAnimal(Animal animal) {
        animals.get(animal.getPosition()).remove(animal);
        //mapChanged("Animal removed from map");
    }

    public synchronized void placePlants(Vector2d plantPosition, Plant plant) {
        plants.put(plantPosition, plant);
        freePositionsForPlants.remove(plantPosition);
    }

    public void spawnPlant(){
        // chceck if there are any free positions
        if (freePositionsForPlants.size() == 0) return;
        // calculate free positions for plants
        Vector2d plantPosition = freePositionsForPlants.get(random.nextInt(freePositionsForPlants.size()));
        //System.out.println(plantPosition);
        if (forestedEquator.willBePlanted(plantPosition)) {
            Plant plant = new Plant(plantPosition);
            placePlants(plantPosition, plant);
        }
        else {
            spawnPlant();
        }
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


    public synchronized void move(Animal animal)  {
        animals.get(animal.getPosition()).remove(animal);
        animal.move(this);
        placeAnimals(animal.getPosition(), animal);
        //mapChanged("Animal moved to "  + animal.getPosition());
    }

    public void reproduce() {
        for (Vector2d position : animals.keySet()) {
            List<Animal> animalList = animals.get(position);
            if (animalList.size() > 1) {
                Animal a1 = animalList.get(0);
                Animal a2 = animalList.get(1);
                if (a1.getEnergy() > simulationProps.getEnergyLevelNeededToReproduce() && a2.getEnergy() > simulationProps.getEnergyLevelNeededToReproduce()) {
                    Animal child = new Animal(position, 2 * simulationProps.getEnergyLevelToPassToChild(),
                            simulationProps.getMaxEnergy(), simulationProps.getDaysElapsed(), Genotype.getGenesFromParents(a1, a2, simulationProps),
                            simulationProps.getMoveStyle());
                    synchronized (this) {
                        animals.get(position).add(child);
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
        Set<Vector2d> keys = new HashSet<>(plants.keySet());
        for ( Vector2d position : keys ){
            if ( animals.containsKey(position) ) {
                List<Animal> animalList = animals.get(position);
                if (animalList.size() > 0) {
                    Animal animal = animalList.get(0);
                    synchronized (this) {
                        animal.eat(simulationProps.getPlantEnergy());
                        plants.remove(position);
                        freePositionsForPlants.add(position);
                    }
                }
            }

        }
    }

    public void growPlants() {
        int plantsToAdd = simulationProps.getSpawnPlantPerDay();
        for (int i = 0; i<plantsToAdd; i++) {
            spawnPlant();
        }
    }

    public synchronized IWorldElement objectAt(Vector2d position) {
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

    public Integer getPlantsCount() {
        return plants.size();
    }

    public Integer getEmptyCount() {
        Set<Vector2d> position = new HashSet<>();
        for (Vector2d pos : animals.keySet())
            if (!animals.get(pos).isEmpty())
                position.add(pos);
        position.addAll(plants.keySet());
        return width*height - position.size();
    }

    public ForestedEquator getForestedEquator() {
        return forestedEquator;
    }
}