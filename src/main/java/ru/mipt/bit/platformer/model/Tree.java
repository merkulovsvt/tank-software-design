package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public class Tree implements FieldObject {

    private final GridPoint2 coordinates;

    public Tree(GridPoint2 coordinates) {
        this.coordinates = new GridPoint2(coordinates);
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void hit(int damage) {
    }

    @Override
    public boolean isDestroyed() {
        return false;
    }

    @Override
    public boolean occupies(GridPoint2 otherCoordinates) {
        return coordinates.equals(otherCoordinates);
    }

    @Override
    public GridPoint2 getCoordinates() {
        return new GridPoint2(coordinates);
    }

    @Override
    public GridPoint2 getDestinationCoordinates() {
        return new GridPoint2(coordinates);
    }

    @Override
    public float getMovementProgress() {
        return 1f;
    }

    @Override
    public float getRotation() {
        return 0f;
    }
}
