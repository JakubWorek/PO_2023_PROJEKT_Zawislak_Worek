package org.proj.model.elements;

import org.junit.jupiter.api.Test;
import org.proj.model.SimulationProps;
import org.proj.model.maps.EMapType;
import org.proj.utils.Vector2d;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnimalTest {
    @Test
    public void testAnimalConstructor() {
        SimulationProps simulationProps = new SimulationProps(5, 5, 1, 0, 0, 0, 100, 10, EMoveStyle.FULLY_PREDESTINED, EMutationStyle.FULLY_RANDOM, EMapType.WATER, 6, 40, 20, 0, false, "csv.csv", 1, 0, 0);
        Animal animal = new Animal(new Vector2d(1,1), simulationProps);
        assert animal.getAge().equals(0);
        assert animal.getMoveStyle().equals(EMoveStyle.FULLY_PREDESTINED);
        assert animal.getEnergy().equals(100);
        assert animal.getBirthDate().equals(0);
        assert animal.getDeathDate().equals(-1);
        assert animal.getGeneIndex().equals(0);
        assert animal.getChildrenMade().equals(0);
        assert animal.getPlantsEaten().equals(0);
        assert animal.getPosition().equals(new Vector2d(1,1));
    }

    @Test
    public void testAnimalIsAt() {
        SimulationProps simulationProps = new SimulationProps(5, 5, 1, 0, 0, 0, 100, 10, EMoveStyle.FULLY_PREDESTINED, EMutationStyle.FULLY_RANDOM, EMapType.WATER, 6, 40, 20, 0, false, "csv.csv", 1, 0, 0);
        int[] genome = new int[8];
        for (int i = 0; i < 8; i++) {
            genome[i] = i;
        }
        Animal animal = new Animal(new Vector2d(1,1), simulationProps);
        assert animal.isAt(new Vector2d(1,1));
        assert !animal.isAt(new Vector2d(1,2));
    }

    @Test
    public void testCountingDescendants() {
        SimulationProps simulationProps = new SimulationProps(5, 5, 1, 0, 0, 0, 100, 10, EMoveStyle.FULLY_PREDESTINED, EMutationStyle.FULLY_RANDOM, EMapType.WATER, 6, 40, 20, 0, false, "csv.csv", 1, 0, 0);
        Animal a1 = new Animal(new Vector2d(0,0), simulationProps);
        Animal a2 = new Animal(new Vector2d(0,0), simulationProps);
        Animal a3 = new Animal(new Vector2d(0,0), simulationProps);
        Animal a4 = new Animal(new Vector2d(0,0), simulationProps);

        a2.addChild();
        a2.addChildToList(a3);
        a2.addChild();
        a2.addChildToList(a4);

        a1.addChild();
        a1.addChildToList(a2);

        assertEquals(3, a1.countDescendants());
    }
}
