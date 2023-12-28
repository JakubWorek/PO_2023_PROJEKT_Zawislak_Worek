package org.proj.model;

import org.proj.model.elements.Animal;
import org.proj.model.elements.EMutationStyle;

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

    public static int[] getGenesFromParents(Animal parent1, Animal parent2, EMutationStyle mutationStyle, int genesCount) {
        Random random = new Random();
        int[] genes = new int[genesCount];
        int[] parent1Genes = parent1.getGenome();
        int[] parent2Genes = parent2.getGenome();
        int parent1Energy = parent1.getEnergy();
        int parent2Energy = parent2.getEnergy();

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
            for (int i = 0; i < genesCount; i++) {
                genes[i] = random.nextInt(0, 8);
            }
        }

        // return finale genes
        return genes;
    }
}
