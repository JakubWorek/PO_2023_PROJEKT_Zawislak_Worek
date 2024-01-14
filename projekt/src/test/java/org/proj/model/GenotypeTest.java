package org.proj.model;

import org.junit.jupiter.api.Test;
import org.proj.model.elements.Animal;
import org.proj.model.elements.EMoveStyle;
import org.proj.model.elements.EMutationStyle;
import org.proj.model.maps.EMapType;
import org.proj.utils.Vector2d;
import static org.junit.jupiter.api.Assertions.*;

public class GenotypeTest {
    @Test
    public void testGetRandomGenes() {
        int[] genes = Genotype.getRandomGenes(32);
        assertEquals(32, genes.length);
        for (int gene : genes) {
            assertTrue(gene >= 0 && gene <= 7);
        }
    }

    @Test
    public void testGetGenesFromParentsWithoutMutations() {
        Animal parent1 = new Animal(new Vector2d(0, 0), 40, 0, 0, Genotype.getRandomGenes(6), EMoveStyle.FULLY_PREDESTINED);
        Animal parent2 = new Animal(new Vector2d(0, 0), 40, 0, 0, Genotype.getRandomGenes(6), EMoveStyle.FULLY_PREDESTINED);

        int[] genes = Genotype.getGenesFromParents( parent1,
                                                    parent2,
                                                    new SimulationProps(0, 0, 0, 0, 0, 0, 0, 100, 0, EMoveStyle.FULLY_PREDESTINED, EMutationStyle.FULLY_RANDOM, EMapType.WATER, 6, 40, 20, 0, false, "csv.csv", 1, 0, 0)
        );
        assertEquals(6, genes.length);
        for (int gene : genes) {
            assertTrue(gene >= 0 && gene <= 7);
        }
        for (int i = 0; i < genes.length; i++) {
            assertTrue(genes[i] == parent1.getGenome()[i] || genes[i] == parent2.getGenome()[i]);
        }

    }

    @Test
    public void testGetGenesFromParentsWith2Mutations(){
        Animal parent1 = new Animal(new Vector2d(0, 0), 40, 0, 0, Genotype.getRandomGenes(6), EMoveStyle.FULLY_PREDESTINED);
        Animal parent2 = new Animal(new Vector2d(0, 0), 40, 0, 0, Genotype.getRandomGenes(6), EMoveStyle.FULLY_PREDESTINED);

        int[] genes = Genotype.getGenesFromParents( parent1,
                                                    parent2,
                                                    new SimulationProps(0, 0, 0, 0, 0, 0, 0, 100, 0, EMoveStyle.FULLY_PREDESTINED, EMutationStyle.FULLY_RANDOM, EMapType.WATER, 6, 40, 20, 0, false, "csv.csv", 1, 0, 2)
        );
        assertEquals(6, genes.length);
        for (int gene : genes) {
            assertTrue(gene >= 0 && gene <= 7);
        }
        int mutations = 0;
        for (int i = 0; i < genes.length; i++) {
            if(genes[i] != parent1.getGenome()[i] && genes[i] != parent2.getGenome()[i]){
                mutations++;
            }
        }
        // mutations should be not greater than 2
        assertTrue(mutations <= 2);
    }
}
