package org.proj.model.maps;

import org.proj.utils.Vector2d;
import java.util.Random;

public class ForestedEquator {
    private final Random random = new Random();
    private final Vector2d equatorLowerLeft;
    private final Vector2d equatorUpperRight;

    public ForestedEquator(int equatorHeight, int mapWidth, int mapHeight) {
        this.equatorLowerLeft = new Vector2d(0, (mapHeight-equatorHeight)/2+1);
        this.equatorUpperRight = new Vector2d(mapWidth-1, (equatorLowerLeft.getY()+equatorHeight-1));
    }

    public boolean isPreferable(Vector2d position) {
        return equatorLowerLeft.precedes(position) && equatorUpperRight.follows(position);
    }

    public boolean willBePlanted(Vector2d position) {
        return isPreferable(position) == (random.nextInt(5) != 4);
    }

    public String groundType(Vector2d position) {
        return isPreferable(position) ? "equator" : "grass";
    }
}
