package org.proj.model.elements;

import org.junit.jupiter.api.Test;
import org.proj.utils.Vector2d;
import static org.junit.jupiter.api.Assertions.*;

public class PlantTest {
    @Test
    public void testGetPosition() {
        Vector2d position = new Vector2d(1, 2);
        Plant plant = new Plant(position);
        assertEquals(position, plant.getPosition());
    }

    @Test
    public void testToString() {
        Vector2d position = new Vector2d(1, 2);
        Plant plant = new Plant(position);
        assertEquals("*", plant.toString());
    }

    @Test
    public void testGetElementType() {
        Vector2d position = new Vector2d(1, 2);
        Plant plant = new Plant(position);
        assertEquals(EElementType.PLANT, plant.getElementType());
    }
}
