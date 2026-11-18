package ru.mipt.bit.platformer.control;

import java.util.List;

// strategy of controlling a tank: decides which commands to run on the current frame
public interface TankController {

    List<GameCommand> nextCommands();
}
