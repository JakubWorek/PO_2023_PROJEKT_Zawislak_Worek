package org.proj.model.elements;

import javafx.scene.paint.Color;
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
    public FieldPaint getFieldPaint() {
        return new FieldPaint(Color.TRANSPARENT, Color.LIGHTBLUE);
    }

    @Override
    public EElementType getElementType() {
        return EElementType.WATER;
    }
}
