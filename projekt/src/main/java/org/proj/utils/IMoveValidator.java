package org.proj.utils;

public interface IMoveValidator {
    PositionOrientationTuple correctPosition(Vector2d position, EMapDirection orientation);
}
