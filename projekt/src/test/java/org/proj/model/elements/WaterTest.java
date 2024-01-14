package org.proj.model.elements;

import org.junit.jupiter.api.Test;
import org.proj.utils.Vector2d;
import static org.junit.jupiter.api.Assertions.*;

public class WaterTest {
    @Test
    public void testGetPosition() {
        Vector2d position = new Vector2d(1, 2);
        Water water = new Water(position);
        assertEquals(position, water.getPosition());
    }

    @Test
    public void testToString() {
        Water water = new Water(new Vector2d(1, 2));
        assertEquals("W", water.toString());
    }

    @Test
    public void testGetElementType() {
        Water water = new Water(new Vector2d(1, 2));
        assertEquals(EElementType.WATER, water.getElementType());
    }
}
