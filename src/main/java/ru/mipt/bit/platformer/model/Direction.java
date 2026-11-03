package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Orientation on the grid. Holds the coordinate shift of one step in this direction
 * and the sprite rotation that matches it, so the "shift coordinates + set rotation"
 * pairs are not copy-pasted around the code any more.
 */
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

    /** Coordinates of the neighbouring tile in this direction. The argument is not modified. */
    public GridPoint2 nextCoordinates(GridPoint2 coordinates) {
        return new GridPoint2(coordinates).add(shiftX, shiftY);
    }

    public float getRotation() {
        return rotation;
    }
}
