package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Domain interface (not a port): whether a tile is free to move into.
 */
public interface Obstacles {

    boolean isFree(GridPoint2 coordinates);
}
