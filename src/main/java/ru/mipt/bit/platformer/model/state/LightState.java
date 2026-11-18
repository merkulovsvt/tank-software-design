package ru.mipt.bit.platformer.model.state;

/**
 * Domain. More than 70% of health: the tank behaves as undamaged.
 */
public class LightState extends HealthState {

    @Override
    public float movementTime(float baseMovementTime) {
        return baseMovementTime;
    }

    @Override
    public boolean canShoot() {
        return true;
    }
}
