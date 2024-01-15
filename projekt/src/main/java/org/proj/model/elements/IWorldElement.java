package org.proj.model.elements;

import org.proj.utils.Vector2d;

public interface IWorldElement {
    Vector2d getPosition();
    FieldPaint getFieldPaint();
    EElementType getElementType();
}
