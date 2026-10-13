package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class Tank implements GameObject {

    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    // seconds per tile
    private final float movementSpeed;
    private float movementProgress = 1f;
    private float rotation = 0f;

    public Tank(GridPoint2 initialCoordinates, float movementSpeed) {
        this.coordinates = new GridPoint2(initialCoordinates);
        this.destinationCoordinates = new GridPoint2(initialCoordinates);
        this.movementSpeed = movementSpeed;
    }

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

    public void update(float deltaTime) {
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
        if (!isMoving()) {
            coordinates.set(destinationCoordinates);
        }
    }

    public boolean isMoving() {
        return !isEqual(movementProgress, 1f);
    }

    @Override
    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    @Override
    public GridPoint2 getDestinationCoordinates() {
        return destinationCoordinates;
    }

    @Override
    public float getMovementProgress() {
        return movementProgress;
    }

    @Override
    public float getRotation() {
        return rotation;
    }
}
