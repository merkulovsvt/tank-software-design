package ru.mipt.bit.platformer.control;

/**
 * Driving port (application): an action the player or the AI asks the game to do.
 */
public interface GameCommand {

    void execute();
}
