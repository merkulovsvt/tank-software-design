package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Tank;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomTankControllerTest {

    @Test
    void restingTankGetsOneCommand() {
        Tank tank = new Tank(new GridPoint2(5, 5), 0.4f);
        RandomTankController controller = new RandomTankController(tank, new GameField(10, 8), new Random(1));

        assertEquals(1, controller.nextCommands().size());
    }

    @Test
    void movingTankGetsNoCommands() {
        GameField field = new GameField(10, 8);
        Tank tank = new Tank(new GridPoint2(5, 5), 0.4f);
        tank.move(Direction.UP, field);
        RandomTankController controller = new RandomTankController(tank, field, new Random(1));

        assertEquals(List.of(), controller.nextCommands());
    }

    @Test
    void soonerOrLaterGoesInEveryDirection() {
        Set<GridPoint2> destinations = new HashSet<>();
        Random random = new Random(42);
        for (int i = 0; i < 100; i++) {
            Tank tank = new Tank(new GridPoint2(5, 5), 0.4f);
            for (GameCommand command : new RandomTankController(tank, new GameField(10, 8), random).nextCommands()) {
                command.execute();
            }
            destinations.add(tank.getDestinationCoordinates());
        }

        assertTrue(destinations.containsAll(Set.of(new GridPoint2(5, 6), new GridPoint2(4, 5),
                new GridPoint2(5, 4), new GridPoint2(6, 5))));
    }

    @Test
    void sometimesShootsInsteadOfMoving() {
        int shots = 0;
        Random random = new Random(42);
        for (int i = 0; i < 100; i++) {
            GameField field = new GameField(10, 8);
            Tank tank = new Tank(new GridPoint2(5, 5), 0.4f);
            field.add(tank);
            for (GameCommand command : new RandomTankController(tank, field, random).nextCommands()) {
                command.execute();
            }
            if (!tank.isMoving() && !field.isFree(new GridPoint2(6, 5))) {
                shots++;
            }
        }

        assertTrue(shots > 0 && shots < 100, "shots: " + shots);
    }
}
