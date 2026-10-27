package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Obstacles;
import ru.mipt.bit.platformer.model.Tank;

public class MoveCommand implements GameCommand {

    private final Tank tank;
    private final Obstacles obstacles;
    private final Direction direction;

    public MoveCommand(Tank tank, Obstacles obstacles, Direction direction) {
        this.tank = tank;
        this.obstacles = obstacles;
        this.direction = direction;
    }

    @Override
    public void execute() {
        tank.move(direction, obstacles);
    }
}
