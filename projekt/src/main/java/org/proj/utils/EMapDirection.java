package org.proj.utils;

public enum EMapDirection {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;

    public String toString() {
        switch(this) {
            case NORTH: return "N";
            case NORTH_EAST: return "NE";
            case EAST: return "E";
            case SOUTH_EAST: return "SE";
            case SOUTH: return "S";
            case SOUTH_WEST: return "SW";
            case WEST: return "W";
            case NORTH_WEST: return "NW";
            default: return "";
        }
    }

    public EMapDirection next() {
        return switch(this) {
            case NORTH -> NORTH_EAST;
            case NORTH_EAST -> EAST;
            case EAST -> SOUTH_EAST;
            case SOUTH_EAST -> SOUTH;
            case SOUTH -> SOUTH_WEST;
            case SOUTH_WEST -> WEST;
            case WEST -> NORTH_WEST;
            case NORTH_WEST -> NORTH;
        };
    }

    public EMapDirection rotate(int angle) {
        EMapDirection result = this;
        for (int i = 0; i < angle; i++) {
            result = result.next();
        }
        return result;
    }

    public Vector2d unitVector() {
        return switch(this) {
            case NORTH -> new Vector2d(0, 1);
            case NORTH_EAST -> new Vector2d(1, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTH_EAST -> new Vector2d(1, -1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTH_WEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(-1, 0);
            case NORTH_WEST -> new Vector2d(-1, 1);
        };
    }
}
