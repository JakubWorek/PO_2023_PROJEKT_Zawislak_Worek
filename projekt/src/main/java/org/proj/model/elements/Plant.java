package org.proj.model.elements;

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
}
