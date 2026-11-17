package ru.mipt.bit.platformer.model.state;

/**
 * Domain. State of a tank (State pattern): how fast it moves and how it shoots.
 * New states (e.g. a booster from the field) are added as new implementations, Tank stays unchanged.
 */
public interface TankState {

    float movementTime(float baseMovementTime);

    float reloadTime(float baseReloadTime);

    boolean canShoot();

    TankState onHealthChanged(float healthShare);
}
