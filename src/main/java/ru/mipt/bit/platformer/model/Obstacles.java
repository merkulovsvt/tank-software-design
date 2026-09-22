package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Question a moving object asks before stepping onto a tile: "is it free?".
 * Lets a tank decide about a move without knowing what kinds of obstacles exist.
 */
public interface Obstacles {

    boolean isFree(GridPoint2 coordinates);
}
