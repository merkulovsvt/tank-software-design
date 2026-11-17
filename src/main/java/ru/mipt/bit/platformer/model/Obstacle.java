package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Domain interface: something that occupies tiles.
 */
public interface Obstacle {

    boolean occupies(GridPoint2 coordinates);
}
