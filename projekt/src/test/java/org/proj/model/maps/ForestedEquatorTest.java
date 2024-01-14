package org.proj.model.maps;

import org.junit.jupiter.api.Test;
import org.proj.utils.Vector2d;

import static org.junit.jupiter.api.Assertions.*;

public class ForestedEquatorTest {
    @Test
    public void testIsPreferable() {
        ForestedEquator forestedEquator = new ForestedEquator(2, 10, 10);
        assertTrue(forestedEquator.isPreferable(new Vector2d(0, 5)));
        assertTrue(forestedEquator.isPreferable(new Vector2d(9, 5)));
        assertFalse(forestedEquator.isPreferable(new Vector2d(0, 4)));
        assertTrue(forestedEquator.isPreferable(new Vector2d(9, 6)));
    }

    @Test
    public void testGroundType(){
        ForestedEquator forestedEquator = new ForestedEquator(2, 10, 10);
        assertEquals("equator", forestedEquator.groundType(new Vector2d(0, 5)));
        assertEquals("equator", forestedEquator.groundType(new Vector2d(9, 5)));
        assertEquals("grass", forestedEquator.groundType(new Vector2d(0, 4)));
        assertEquals("equator", forestedEquator.groundType(new Vector2d(9, 6)));
    }
}
