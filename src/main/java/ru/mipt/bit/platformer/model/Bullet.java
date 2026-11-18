package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class Bullet implements FieldObject {

    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private final Direction direction;
    private final float movementSpeed;
    private final int damage;
    private final Battlefield battlefield;
    private float movementProgress = 1f;
    private boolean destroyed = false;

    public Bullet(GridPoint2 start, Direction direction, float movementSpeed, int damage, Battlefield battlefield) {
        this.coordinates = new GridPoint2(start);
        this.destinationCoordinates = new GridPoint2(start);
        this.direction = direction;
        this.movementSpeed = movementSpeed;
        this.damage = damage;
        this.battlefield = battlefield;
    }

    @Override
    public void update(float deltaTime) {
        if (destroyed) {
            return;
        }
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
        if (!isEqual(movementProgress, 1f)) {
            return;
        }
        coordinates.set(destinationCoordinates);
        GridPoint2 next = direction.nextCoordinates(coordinates);
        if (battlefield.isFree(next)) {
            destinationCoordinates.set(next);
            movementProgress = 0f;
        } else {
            destroy();
            battlefield.hit(next, damage);
        }
    }

    @Override
    public void hit(int damage) {
        destroy();
    }

    private void destroy() {
        destroyed = true;
        battlefield.remove(this);
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public boolean occupies(GridPoint2 otherCoordinates) {
        return coordinates.equals(otherCoordinates) || destinationCoordinates.equals(otherCoordinates);
    }

    @Override
    public GridPoint2 getCoordinates() {
        return new GridPoint2(coordinates);
    }

    @Override
    public GridPoint2 getDestinationCoordinates() {
        return new GridPoint2(destinationCoordinates);
    }

    @Override
    public float getMovementProgress() {
        return movementProgress;
    }

    @Override
    public float getRotation() {
        return direction.getRotation();
    }
}
