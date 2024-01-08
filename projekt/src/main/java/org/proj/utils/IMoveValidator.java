package org.proj.utils;

public interface IMoveValidator {
    PositionOrientationTuple correctPosition(Vector2d oldPosition, Vector2d newPosition, EMapDirection orientation);
}
