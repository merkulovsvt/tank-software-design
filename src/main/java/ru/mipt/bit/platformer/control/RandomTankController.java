package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Battlefield;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Tank;

import java.util.List;
import java.util.Random;

public class RandomTankController implements TankController {

    private static final Direction[] DIRECTIONS = Direction.values();
    private static final float SHOOT_CHANCE = 0.25f;

    private final Tank tank;
    private final Battlefield battlefield;
    private final Random random;

    public RandomTankController(Tank tank, Battlefield battlefield, Random random) {
        this.tank = tank;
        this.battlefield = battlefield;
        this.random = random;
    }

    @Override
    public List<GameCommand> nextCommands() {
        if (tank.isMoving()) {
            return List.of();
        }
        if (random.nextFloat() < SHOOT_CHANCE) {
            return List.of(new ShootCommand(tank, battlefield));
        }
        Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
        return List.of(new MoveCommand(tank, battlefield, direction));
    }
}
