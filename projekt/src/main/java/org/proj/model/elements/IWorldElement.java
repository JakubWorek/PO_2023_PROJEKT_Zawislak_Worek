package org.proj.model.elements;

import javafx.scene.shape.Shape;
import org.proj.utils.Vector2d;

public interface IWorldElement {
    Vector2d getPosition();
    Shape getShapeToPrint(int cellSize);
}
