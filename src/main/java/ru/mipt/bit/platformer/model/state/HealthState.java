package ru.mipt.bit.platformer.model.state;

/**
 * Domain. Base of the states that depend only on the health left.
 */
public abstract class HealthState implements TankState {

    private static final float LIGHT_HEALTH_SHARE = 0.7f;
    private static final float HEAVY_HEALTH_SHARE = 0.15f;

    public static TankState of(float healthShare) {
        if (healthShare > LIGHT_HEALTH_SHARE) {
            return new LightState();
        }
        if (healthShare >= HEAVY_HEALTH_SHARE) {
            return new MediumState();
        }
        return new HeavyState();
    }

    @Override
    public float reloadTime(float baseReloadTime) {
        return baseReloadTime;
    }

    @Override
    public TankState onHealthChanged(float healthShare) {
        return of(healthShare);
    }
}
