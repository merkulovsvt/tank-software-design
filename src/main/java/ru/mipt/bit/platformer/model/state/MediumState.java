package ru.mipt.bit.platformer.model.state;

/**
 * Domain. From 15% to 70% of health: the tank moves 2 times slower.
 */
public class MediumState extends HealthState {

    private static final float SLOWDOWN = 2f;

    @Override
    public float movementTime(float baseMovementTime) {
        return baseMovementTime * SLOWDOWN;
    }

    @Override
    public boolean canShoot() {
        return true;
    }
}
