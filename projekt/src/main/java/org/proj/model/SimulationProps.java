package org.proj.model;

import org.proj.model.elements.EMoveStyle;
import org.proj.model.elements.EMutationStyle;

public class SimulationProps {
    private int width;
    private int height;
    private int animalCount;
    private int plantCount;
    private int energy;
    private int maxEnergy;
    private int plantEnergy;
    private EMoveStyle moveStyle;
    private EMutationStyle mutationStyle;
    private int genesCount;
    private int energyLevelNeededToReproduce;
    private int energyLevelToPassToChild;
    private int daysElapsed;

    public SimulationProps(int width_, int height_, int animalCount_, int plantCount_, int energy_, int maxEnergy_, int plantEnergy_, EMoveStyle moveStyle_, EMutationStyle mutationStyle_, int genesCount_, int energyLevelNeededToReproduce_, int energyLevelToPassToChild_) {
        width = width_;
        height = height_;
        animalCount = animalCount_;
        plantCount = plantCount_;
        energy = energy_;
        maxEnergy = maxEnergy_;
        plantEnergy = plantEnergy_;
        moveStyle = moveStyle_;
        mutationStyle = mutationStyle_;
        genesCount = genesCount_;
        energyLevelNeededToReproduce = energyLevelNeededToReproduce_;
        energyLevelToPassToChild = energyLevelToPassToChild_;
        daysElapsed = 0;
    }

    public synchronized void incrementDaysElapsed() {
        daysElapsed++;
    }

    public synchronized int getMapWidth(){
        return width;
    }

    public synchronized int getMapHeight(){
        return height;
    }

    public synchronized int getStartAnimalCount(){
        return animalCount;
    }

    public synchronized int getStartPlantCount(){
        return plantCount;
    }

    public synchronized int getStartEnergy() {
        return energy;
    }

    public synchronized EMoveStyle getMoveStyle() {
        return moveStyle;
    }

    public synchronized EMutationStyle getMutationStyle() { return mutationStyle; }

    public synchronized int getMaxEnergy() {
        return maxEnergy;
    }

    public synchronized int getPlantEnergy(){
        return plantEnergy;
    }

    public synchronized int getGenesCount() {
        return genesCount;
    }

    public synchronized int getEnergyLevelNeededToReproduce() {
        return energyLevelNeededToReproduce;
    }

    public synchronized int getEnergyLevelToPassToChild() {
        return energyLevelToPassToChild;
    }

    public synchronized int getDaysElapsed() {
        return daysElapsed;
    }

}
