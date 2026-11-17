package ru.mipt.bit.platformer.model;

/**
 * Domain interface: an object living on the field.
 */
public interface FieldObject extends GameObject, Obstacle {

    void update(float deltaTime);

    void hit(int damage);

    boolean isDestroyed();
}
