package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.state.LightState;
import ru.mipt.bit.platformer.model.state.TankState;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

/**
 * Domain entity. Speed and ability to shoot depend on its {@link TankState}.
 * Uses libGDX GridPoint2/MathUtils and util.GdxGameUtils: the domain is not independent of infrastructure yet.
 */
public class Tank implements FieldObject, LivingObject {

    private static final int MAX_HEALTH = 100;
    private static final float RELOAD_TIME = 0.5f;
    private static final float BULLET_SPEED = 0.1f;
    private static final int BULLET_DAMAGE = 30;

    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    // seconds per tile
    private final float movementSpeed;
    private float movementProgress = 1f;
    private Direction direction = Direction.RIGHT;
    private int health = MAX_HEALTH;
    private float reloadLeft = 0f;
    private TankState state = new LightState();

    public Tank(GridPoint2 initialCoordinates, float movementSpeed) {
        this.coordinates = new GridPoint2(initialCoordinates);
        this.destinationCoordinates = new GridPoint2(initialCoordinates);
        this.movementSpeed = movementSpeed;
    }

    public void move(Direction direction, Obstacles obstacles) {
        if (isMoving() || isDestroyed()) {
            return;
        }
        GridPoint2 destination = direction.nextCoordinates(coordinates);
        if (obstacles.isFree(destination)) {
            destinationCoordinates.set(destination);
            movementProgress = 0f;
        }
        this.direction = direction;
    }

    public void shoot(Battlefield battlefield) {
        if (isMoving() || isDestroyed() || reloadLeft > 0f || !state.canShoot()) {
            return;
        }
        reloadLeft = state.reloadTime(RELOAD_TIME);
        GridPoint2 start = direction.nextCoordinates(coordinates);
        if (battlefield.isFree(start)) {
            battlefield.add(new Bullet(start, direction, BULLET_SPEED, BULLET_DAMAGE, battlefield));
        } else {
            battlefield.hit(start, BULLET_DAMAGE);
        }
    }

    @Override
    public void update(float deltaTime) {
        reloadLeft = Math.max(0f, reloadLeft - deltaTime);
        movementProgress = continueProgress(movementProgress, deltaTime, state.movementTime(movementSpeed));
        if (!isMoving()) {
            coordinates.set(destinationCoordinates);
        }
    }

    @Override
    public void hit(int damage) {
        health -= damage;
        state = state.onHealthChanged((float) health / MAX_HEALTH);
    }

    @Override
    public boolean isDestroyed() {
        return health <= 0;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public int getMaxHealth() {
        return MAX_HEALTH;
    }

    public boolean isMoving() {
        return !isEqual(movementProgress, 1f);
    }

    // while moving the tank holds both tiles: the one it leaves and the one it goes to
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
