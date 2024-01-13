package org.proj.model.elements;

import javafx.geometry.Insets;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
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

    @Override
    public FieldPaint getFieldPaint() {
        return new FieldPaint(Color.TRANSPARENT, new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(4,4,4,4, false), new Insets(1,1,1,1)));
    }
}
