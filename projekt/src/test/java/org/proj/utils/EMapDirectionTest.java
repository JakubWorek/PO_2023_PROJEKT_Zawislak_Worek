package org.proj.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EMapDirectionTest {
    @Test
    public void testToString() {
        assertEquals("N", EMapDirection.NORTH.toString());
        assertEquals("NE", EMapDirection.NORTH_EAST.toString());
        assertEquals("E", EMapDirection.EAST.toString());
        assertEquals("SE", EMapDirection.SOUTH_EAST.toString());
        assertEquals("S", EMapDirection.SOUTH.toString());
        assertEquals("SW", EMapDirection.SOUTH_WEST.toString());
        assertEquals("W", EMapDirection.WEST.toString());
        assertEquals("NW", EMapDirection.NORTH_WEST.toString());
    }

    @Test
    public void testNext() {
        assertEquals(EMapDirection.NORTH_EAST, EMapDirection.NORTH.next());
        assertEquals(EMapDirection.EAST, EMapDirection.NORTH_EAST.next());
        assertEquals(EMapDirection.SOUTH_EAST, EMapDirection.EAST.next());
        assertEquals(EMapDirection.SOUTH, EMapDirection.SOUTH_EAST.next());
        assertEquals(EMapDirection.SOUTH_WEST, EMapDirection.SOUTH.next());
        assertEquals(EMapDirection.WEST, EMapDirection.SOUTH_WEST.next());
        assertEquals(EMapDirection.NORTH_WEST, EMapDirection.WEST.next());
        assertEquals(EMapDirection.NORTH, EMapDirection.NORTH_WEST.next());
    }

    @Test
    public void testRotate() {
        assertEquals(EMapDirection.NORTH, EMapDirection.NORTH.rotate(0));
        assertEquals(EMapDirection.NORTH_EAST, EMapDirection.NORTH.rotate(1));
        assertEquals(EMapDirection.EAST, EMapDirection.NORTH.rotate(2));
        assertEquals(EMapDirection.SOUTH_EAST, EMapDirection.NORTH.rotate(3));
        assertEquals(EMapDirection.SOUTH, EMapDirection.NORTH.rotate(4));
        assertEquals(EMapDirection.SOUTH_WEST, EMapDirection.NORTH.rotate(5));
        assertEquals(EMapDirection.WEST, EMapDirection.NORTH.rotate(6));
        assertEquals(EMapDirection.NORTH_WEST, EMapDirection.NORTH.rotate(7));
    }

    @Test
    public void testGetRandomDirection() {
        for (int i = 0; i < 100; i++) {
            EMapDirection direction = EMapDirection.getRandomDirection();
            assertTrue(direction == EMapDirection.NORTH ||
                       direction == EMapDirection.NORTH_EAST ||
                       direction == EMapDirection.EAST ||
                       direction == EMapDirection.SOUTH_EAST ||
                       direction == EMapDirection.SOUTH ||
                       direction == EMapDirection.SOUTH_WEST ||
                       direction == EMapDirection.WEST ||
                       direction == EMapDirection.NORTH_WEST);
        }
    }

    @Test
    public void testUnitVector() {
        assertEquals(new Vector2d(0, 1), EMapDirection.NORTH.unitVector());
        assertEquals(new Vector2d(1, 1), EMapDirection.NORTH_EAST.unitVector());
        assertEquals(new Vector2d(1, 0), EMapDirection.EAST.unitVector());
        assertEquals(new Vector2d(1, -1), EMapDirection.SOUTH_EAST.unitVector());
        assertEquals(new Vector2d(0, -1), EMapDirection.SOUTH.unitVector());
        assertEquals(new Vector2d(-1, -1), EMapDirection.SOUTH_WEST.unitVector());
        assertEquals(new Vector2d(-1, 0), EMapDirection.WEST.unitVector());
        assertEquals(new Vector2d(-1, 1), EMapDirection.NORTH_WEST.unitVector());
    }
}
