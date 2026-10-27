package ru.mipt.bit.platformer.control;

/**
 * One thing the player can ask the game to do, independent of the key that triggers it.
 * A future handler (e.g. firing on SPACE) is just another implementation bound to a key.
 */
public interface GameCommand {

    void execute();
}
