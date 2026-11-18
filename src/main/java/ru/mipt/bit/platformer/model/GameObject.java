package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Domain interface: read-only view of an object for the outside world, part of the GameFieldListener port.
 */
public interface GameObject {

    GridPoint2 getCoordinates();

    GridPoint2 getDestinationCoordinates();

    float getMovementProgress();

    float getRotation();
}
