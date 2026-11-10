package ru.mipt.bit.platformer.model;

public interface FieldObject extends GameObject, Obstacle {

    void update(float deltaTime);

    void hit(int damage);

    boolean isDestroyed();
}
