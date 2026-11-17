package ru.mipt.bit.platformer.control;

import java.util.List;

/**
 * Driving port (application). Strategy of controlling a tank: decides which commands to run on the current frame.
 */
public interface TankController {

    List<GameCommand> nextCommands();
}
