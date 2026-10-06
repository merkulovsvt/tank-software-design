package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public enum Direction {

    UP(0, 1, 90f),
    LEFT(-1, 0, -180f),
    DOWN(0, -1, -90f),
    RIGHT(1, 0, 0f);

    private final int shiftX;
    private final int shiftY;
    private final float rotation;

    Direction(int shiftX, int shiftY, float rotation) {
        this.shiftX = shiftX;
        this.shiftY = shiftY;
        this.rotation = rotation;
    }

    public GridPoint2 nextCoordinates(GridPoint2 coordinates) {
        return new GridPoint2(coordinates).add(shiftX, shiftY);
    }

    public float getRotation() {
        return rotation;
    }
}
