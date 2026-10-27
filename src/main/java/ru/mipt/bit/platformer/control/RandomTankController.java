package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Obstacles;
import ru.mipt.bit.platformer.model.Tank;

import java.util.List;
import java.util.Random;

// simplest AI: every frame asks the tank to go in a random direction,
// the tank itself ignores it while it is still moving between tiles
public class RandomTankController implements TankController {

    private static final Direction[] DIRECTIONS = Direction.values();

    private final Tank tank;
    private final Obstacles obstacles;
    private final Random random;

    public RandomTankController(Tank tank, Obstacles obstacles, Random random) {
        this.tank = tank;
        this.obstacles = obstacles;
        this.random = random;
    }

    @Override
    public List<GameCommand> nextCommands() {
        if (tank.isMoving()) {
            return List.of();
        }
        Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
        return List.of(new MoveCommand(tank, obstacles, direction));
    }
}
