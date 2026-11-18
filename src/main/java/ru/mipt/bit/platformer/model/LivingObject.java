package ru.mipt.bit.platformer.model;

/**
 * Domain interface: GameObject with health, read by the health bar.
 */
public interface LivingObject extends GameObject {

    int getHealth();

    int getMaxHealth();
}
