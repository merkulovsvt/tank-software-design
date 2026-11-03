package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveCommandTest {

    @Test
    void executeMovesItsTankInItsDirection() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        GameField field = new GameField(10, 8);
        field.addTank(tank);

        new MoveCommand(tank, field, Direction.UP).execute();

        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
        assertEquals(Direction.UP.getRotation(), tank.getRotation());
    }

    @Test
    void executeChecksObstaclesOfTheGivenField() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        GameField field = new GameField(10, 8);
        field.addTank(tank);
        field.addObstacle(new Tree(new GridPoint2(1, 2)));

        new MoveCommand(tank, field, Direction.UP).execute();

        assertFalse(tank.isMoving());
        assertEquals(Direction.UP.getRotation(), tank.getRotation());
    }
}
