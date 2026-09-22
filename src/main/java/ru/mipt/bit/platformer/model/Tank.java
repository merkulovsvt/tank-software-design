package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

/**
 * A tank: where it stands, where it is heading, how far along the move it is and which
 * way it looks. Pure state — no textures, no keys, no rendering.
 */
public class Tank {

    /** Seconds it takes to cross one tile. */
    private static final float MOVEMENT_SPEED = 0.4f;

    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private float rotation = 0f;

    public Tank(GridPoint2 initialCoordinates) {
        this.coordinates = new GridPoint2(initialCoordinates);
        this.destinationCoordinates = new GridPoint2(initialCoordinates);
    }

    /**
     * Try to step one tile in {@code direction}. A tile taken by an obstacle is not entered —
     * the tank only turns. A move already in progress is never interrupted.
     */
    public void move(Direction direction, Obstacles obstacles) {
        if (isMoving()) {
            return;
        }
        GridPoint2 destination = direction.nextCoordinates(coordinates);
        if (obstacles.isFree(destination)) {
            destinationCoordinates.set(destination);
            movementProgress = 0f;
        }
        rotation = direction.getRotation();
    }

    /** Advance the current move by the time passed since the previous frame. */
    public void update(float deltaTime) {
        movementProgress = continueProgress(movementProgress, deltaTime, MOVEMENT_SPEED);
        if (!isMoving()) {
            // the tank has reached its destination
            coordinates.set(destinationCoordinates);
        }
    }

    public boolean isMoving() {
        return !isEqual(movementProgress, 1f);
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    public GridPoint2 getDestinationCoordinates() {
        return destinationCoordinates;
    }

    public float getMovementProgress() {
        return movementProgress;
    }

    public float getRotation() {
        return rotation;
    }
}
