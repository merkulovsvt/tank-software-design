package ru.mipt.bit.platformer.model.state;

/**
 * Domain. Less than 15% of health: the tank moves 3 times slower and cannot shoot.
 */
public class HeavyState extends HealthState {

    private static final float SLOWDOWN = 3f;

    @Override
    public float movementTime(float baseMovementTime) {
        return baseMovementTime * SLOWDOWN;
    }

    @Override
    public boolean canShoot() {
        return false;
    }
}
