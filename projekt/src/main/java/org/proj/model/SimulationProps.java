package org.proj.model;

import org.proj.model.elements.EMoveStyle;
import org.proj.model.elements.EMutationStyle;

public class SimulationProps {
    static private int energy;
    static private int maxEnergy;
    static private EMoveStyle moveStyle;
    static private EMutationStyle mutationStyle;
    static private int genesCount;
    static private int energyLevelNeededToReproduce;
    static private int energyLevelToPassToChild;
    static private int daysElapsed;

    public static void setProps(int energy_, int maxEnergy_, EMoveStyle moveStyle_, EMutationStyle mutationStyle_, int genesCount_, int energyLevelNeededToReproduce_, int energyLevelToPassToChild_) {
        energy = energy_;
        maxEnergy = maxEnergy_;
        moveStyle = moveStyle_;
        mutationStyle = mutationStyle_;
        genesCount = genesCount_;
        energyLevelNeededToReproduce = energyLevelNeededToReproduce_;
        energyLevelToPassToChild = energyLevelToPassToChild_;
        daysElapsed = 0;
    }

    static void incrementDaysElapsed() {
        daysElapsed++;
    }

    public static int getStartEnergy() {
        return energy;
    }

    public static EMoveStyle getMoveStyle() {
        return moveStyle;
    }

    public static EMutationStyle getMutationStyle() { return mutationStyle; }

    public static int getMaxEnergy() {
        return maxEnergy;
    }

    public static int getGenesCount() {
        return genesCount;
    }

    public static int getEnergyLevelNeededToReproduce() {
        return energyLevelNeededToReproduce;
    }

    public static int getEnergyLevelToPassToChild() {
        return energyLevelToPassToChild;
    }

    public static int getDaysElapsed() {
        return daysElapsed;
    }

    private SimulationProps() {

    }
}
