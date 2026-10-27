package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/**
 * A tree: a static obstacle that occupies exactly one tile. Pure state, no rendering.
 */
public class Tree {

    private final GridPoint2 coordinates;

    public Tree(GridPoint2 coordinates) {
        this.coordinates = new GridPoint2(coordinates);
    }

    public boolean occupies(GridPoint2 otherCoordinates) {
        return coordinates.equals(otherCoordinates);
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }
}
