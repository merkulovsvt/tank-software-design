package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Tank;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShootCommandTest {

    @Test
    void executeMakesItsTankShootOnTheGivenField() {
        GameField field = new GameField(10, 8);
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        field.add(tank);
        assertTrue(field.isFree(new GridPoint2(2, 1)));

        new ShootCommand(tank, field).execute();

        assertFalse(field.isFree(new GridPoint2(2, 1)));
    }
}
