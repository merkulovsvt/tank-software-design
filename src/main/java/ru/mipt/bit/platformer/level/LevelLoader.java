package ru.mipt.bit.platformer.level;

/**
 * Driven port (application): where the level layout comes from.
 */
public interface LevelLoader {

    LevelLayout load();
}
