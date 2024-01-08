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
    private List<Vector2d> freePositionsForPlants = new ArrayList<>();
    protected int width;
    protected int height;
    private final ForestedEquator forestedEquator;
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

        forestedEquator = new ForestedEquator(this.simulationProps.getEquatorHeight(), width, height);

        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                Vector2d position = new Vector2d(x,y);
                freePositionsForPlants.add(position);
                //System.out.println(position);
            }
        }
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

    public void removeAnimal(Animal animal) {
        animals.get(animal.getPosition()).remove(animal);
    }

    public void placePlants(Vector2d plantPosition, Plant plant) {
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
        Set<Vector2d> keys = new HashSet<>(plants.keySet());
        for ( Vector2d position : keys ){
            if ( animals.containsKey(position) ) {
                List<Animal> animalList = animals.get(position);
                if (animalList.size() > 0) {
                    Animal animal = animalList.get(0);
                    for (Animal animal1 : animalList) {
                        animal = animal.compareWith(animal1);
                    }
                    animal.eat(simulationProps.getPlantEnergy());
                    plants.remove(position);
                    freePositionsForPlants.add(position);
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