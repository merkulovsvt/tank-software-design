package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Tank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveCommandTest {

    @Test
    void executeAsksTheFieldToMoveThePlayerInItsDirection() {
        Tank tank = new Tank(new GridPoint2(1, 1));
        GameField field = new GameField(tank);

        new MoveCommand(field, Direction.UP).execute();

        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
        assertEquals(Direction.UP.getRotation(), tank.getRotation());
    }
}
