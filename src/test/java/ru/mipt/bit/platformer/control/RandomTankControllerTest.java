package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Obstacles;
import ru.mipt.bit.platformer.model.Tank;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomTankControllerTest {

    private static final Obstacles ALL_FREE = coordinates -> true;

    @Test
    void restingTankGetsOneMoveCommand() {
        Tank tank = new Tank(new GridPoint2(5, 5), 0.4f);
        RandomTankController controller = new RandomTankController(tank, ALL_FREE, new Random(1));

        List<GameCommand> commands = controller.nextCommands();
        assertEquals(1, commands.size());

        commands.get(0).execute();
        assertTrue(tank.isMoving());
    }

    @Test
    void movingTankGetsNoCommands() {
        Tank tank = new Tank(new GridPoint2(5, 5), 0.4f);
        tank.move(Direction.UP, ALL_FREE);
        RandomTankController controller = new RandomTankController(tank, ALL_FREE, new Random(1));

        assertEquals(List.of(), controller.nextCommands());
    }

    @Test
    void soonerOrLaterGoesInEveryDirection() {
        Set<GridPoint2> destinations = new HashSet<>();
        Random random = new Random(42);
        for (int i = 0; i < 100; i++) {
            Tank tank = new Tank(new GridPoint2(5, 5), 0.4f);
            for (GameCommand command : new RandomTankController(tank, ALL_FREE, random).nextCommands()) {
                command.execute();
            }
            destinations.add(tank.getDestinationCoordinates());
        }

        assertEquals(Set.of(new GridPoint2(5, 6), new GridPoint2(4, 5),
                new GridPoint2(5, 4), new GridPoint2(6, 5)), destinations);
    }
}
