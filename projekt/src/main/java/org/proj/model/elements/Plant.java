package org.proj.model.elements;

import javafx.scene.shape.Shape;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.proj.utils.Vector2d;

public class Plant implements IWorldElement {
    private Vector2d position;

    public Plant(Vector2d position) {
        this.position = position;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public Shape getShapeToPrint(int cellSize) {
        return new Circle(cellSize/3, Color.GREEN);
    }
}
