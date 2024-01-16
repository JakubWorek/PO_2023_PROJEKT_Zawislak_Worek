package org.proj.model.elements;

import org.junit.jupiter.api.Test;
import org.proj.utils.Vector2d;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnimalTest {
    @Test
    public void testAnimalConstructor() {
        int[] genome = new int[8];
        for (int i = 0; i < 8; i++) {
            genome[i] = i;
        }
        Animal animal = new Animal(new Vector2d(1,1), 0, 0, 0, genome, EMoveStyle.FULLY_PREDESTINED);
        assert animal.getAge().equals(0);
        assert animal.getMoveStyle().equals(EMoveStyle.FULLY_PREDESTINED);
        assert animal.getEnergy().equals(0);
        assert animal.getBirthDate().equals(0);
        assert animal.getDeathDate().equals(-1);
        assert animal.getGeneIndex().equals(0);
        assert animal.getChildrenMade().equals(0);
        assert animal.getPlantsEaten().equals(0);
        assert animal.getGenome().equals(genome);
        assert animal.getPosition().equals(new Vector2d(1,1));
    }

    @Test
    public void testAnimalIsAt() {
        int[] genome = new int[8];
        for (int i = 0; i < 8; i++) {
            genome[i] = i;
        }
        Animal animal = new Animal(new Vector2d(1,1), 0, 0, 0, genome, EMoveStyle.FULLY_PREDESTINED);
        assert animal.isAt(new Vector2d(1,1));
        assert !animal.isAt(new Vector2d(1,2));
    }

    @Test
    public void testCountingDescendants() {
        int[] genome = new int[8];
        for (int i = 0; i < 8; i++) {
            genome[i] = i;
        }
        Animal a1 = new Animal(new Vector2d(0,0), 1, 100, 1, genome, EMoveStyle.FULLY_PREDESTINED);
        Animal a2 = new Animal(new Vector2d(0,0), 2, 100, 1, genome, EMoveStyle.FULLY_PREDESTINED);
        Animal a3 = new Animal(new Vector2d(0,0), 3, 100, 1, genome, EMoveStyle.FULLY_PREDESTINED);
        Animal a4 = new Animal(new Vector2d(0,0), 4, 100, 1, genome, EMoveStyle.FULLY_PREDESTINED);

        a2.addChild();
        a2.addChildToList(a3);
        a2.addChild();
        a2.addChildToList(a4);

        a1.addChild();
        a1.addChildToList(a2);

        assertEquals(3, a1.countDescendants());
    }
}
