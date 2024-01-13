package org.proj.model.elements;

import javafx.scene.shape.Shape;
import org.proj.utils.Vector2d;

import java.lang.annotation.ElementType;

public interface IWorldElement {
    Vector2d getPosition();
    FieldPaint getFieldPaint();

    EElementType getElementType();
}
