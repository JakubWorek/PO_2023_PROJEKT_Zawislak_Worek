package org.proj.model.elements;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import org.proj.utils.Vector2d;

public class Water implements IWorldElement{
    private Vector2d position;

    public Water(Vector2d position) {
        this.position = position;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    @Override
    public String toString() {
        return "W";
    }

    public Shape getShapeToPrint(int cellSize) {
        return null;
    }
}
