package org.proj.model;

import org.proj.model.elements.Animal;
import org.proj.model.elements.EMutationStyle;

import java.util.ArrayList;
import java.util.Random;

public class Genotype {
    // static class so private constructor
    private Genotype () { }

    public static int[] getRandomGenes(int genesCount) {
        Random random = new Random();
        int[] genes = new int[genesCount];
        for (int i = 0; i < genesCount; i++){
            genes[i] = random.nextInt(0, 7);
        }
        return genes;
    }

    public static int[] getGenesFromParents(Animal parent1, Animal parent2, SimulationProps simulationProps) {
        Random random = new Random();
        int genesCount = simulationProps.getGenesCount();
        EMutationStyle mutationStyle = simulationProps.getMutationStyle();
        int[] genes = new int[genesCount];
        int[] parent1Genes = parent1.getGenome();
        int[] parent2Genes = parent2.getGenome();
        int parent1Energy = parent1.getEnergy();
        int parent2Energy = parent2.getEnergy();
        int minMutation = simulationProps.getMinMutation();
        int maxMutation = simulationProps.getMaxMutation();

        // calculate split point
        int splitPoint = (int) (((double) parent1Energy / (double)(parent1Energy + parent2Energy))*genesCount);

        // draw left or right side of genes from parent with more energy, true = left, false = right
        if (random.nextBoolean()){
            for (int i = 0; i < splitPoint; i++){
                genes[i] = parent1Genes[i];
            }
            for (int i = splitPoint; i < genesCount; i++){
                genes[i] = parent2Genes[i];
            }
        } else {
            for (int i = 0; i < splitPoint; i++){
                genes[i] = parent2Genes[i];
            }
            for (int i = splitPoint; i < genesCount; i++){
                genes[i] = parent1Genes[i];
            }
        }

        // mutate
        if (mutationStyle == EMutationStyle.FULLY_RANDOM) {
            ArrayList<Integer> indexes = new ArrayList<>();
            for (int i = 0; i < genes.length; i++) {indexes.add(i);}
            if (minMutation > genes.length) {minMutation = genes.length;}
            if (maxMutation > genes.length) {maxMutation = genes.length;}
            if (minMutation > maxMutation) {minMutation = maxMutation;}
            if (minMutation < 0) {minMutation = 0;}
            if (maxMutation < 0) {maxMutation = 0;}
            if (minMutation == 0 && maxMutation == 0) {return genes;}
            int numberOfMutations = random.nextInt(maxMutation - minMutation) + minMutation;
            for (int counter = 0; counter < numberOfMutations; counter++) {
                int i = indexes.remove(random.nextInt(indexes.size()));
                genes[i] = random.nextInt(8);
            }
        }

        // return finale genes
        return genes;
    }
}
