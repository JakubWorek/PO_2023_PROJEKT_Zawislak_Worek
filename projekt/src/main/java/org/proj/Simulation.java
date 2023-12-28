package org.proj;

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
            // eat
            map.eat();
            // reproduce animals
            map.reproduce();
            // remove dead animals
            System.out.println("X");
        }
    }

    public void addAnimal(Animal animal){
        animals.add(animal);
    }
}
