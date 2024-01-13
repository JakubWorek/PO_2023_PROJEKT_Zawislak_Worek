package org.proj.model.elements;

import javafx.geometry.Insets;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
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
    public FieldPaint getFieldPaint() {
        return new FieldPaint(Color.GREEN, new BackgroundFill(Color.TRANSPARENT, new CornerRadii(4,4,4,4, false), new Insets(1,1,1,1)));
    }
}
