package org.proj.model;

import org.proj.model.elements.Animal;
import org.proj.model.elements.EMutationStyle;

import java.util.Random;

public class Genotype {
    // static class so private constructor
    private Genotype () { }

    public static int[] getRandomGenes() {
        Random random = new Random();
        int[] genes = new int[SimulationProps.getGenesCount()];
        for (int i = 0; i < SimulationProps.getGenesCount(); i++){
            genes[i] = random.nextInt(0, 7);
        }
        return genes;
    }

    public static int[] getGenesFromParents(Animal parent1, Animal parent2) {
        Random random = new Random();
        EMutationStyle mutationStyle = SimulationProps.getMutationStyle();
        int[] genes = new int[SimulationProps.getGenesCount()];
        int[] parent1Genes = parent1.getGenome();
        int[] parent2Genes = parent2.getGenome();
        int parent1Energy = parent1.getEnergy();
        int parent2Energy = parent2.getEnergy();

        // calculate split point
        int splitPoint = (int) (((double) parent1Energy / (double)(parent1Energy + parent2Energy))*SimulationProps.getGenesCount());

        // draw left or right side of genes from parent with more energy, true = left, false = right
        if (random.nextBoolean()){
            for (int i = 0; i < splitPoint; i++){
                genes[i] = parent1Genes[i];
            }
            for (int i = splitPoint; i < SimulationProps.getGenesCount(); i++){
                genes[i] = parent2Genes[i];
            }
        } else {
            for (int i = 0; i < splitPoint; i++){
                genes[i] = parent2Genes[i];
            }
            for (int i = splitPoint; i < SimulationProps.getGenesCount(); i++){
                genes[i] = parent1Genes[i];
            }
        }

        // mutate
        if (mutationStyle == EMutationStyle.FULLY_RANDOM) {

        }

        // return finale genes
        return genes;
    }
}
