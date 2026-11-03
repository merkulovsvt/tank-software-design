package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameFieldTest {

    @Test
    void movePlayerIntoAFreeTileStartsTheMove() {
        Tank tank = new Tank(new GridPoint2(1, 1));
        GameField field = new GameField(tank);

        field.movePlayer(Direction.RIGHT);

        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(2, 1), tank.getDestinationCoordinates());
    }

    @Test
    void movePlayerIntoATreeOnlyTurnsTheTank() {
        Tank tank = new Tank(new GridPoint2(1, 1));
        GameField field = new GameField(tank);
        field.addTree(new Tree(new GridPoint2(1, 2)));

        field.movePlayer(Direction.UP);

        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(Direction.UP.getRotation(), tank.getRotation());
    }

    @Test
    void isFreeIsFalseOnTreesAndTrueElsewhere() {
        GameField field = new GameField(new Tank(new GridPoint2(0, 0)));
        field.addTree(new Tree(new GridPoint2(2, 2)));

        assertFalse(field.isFree(new GridPoint2(2, 2)));
        assertTrue(field.isFree(new GridPoint2(3, 3)));
    }

    @Test
    void updateAdvancesThePlayer() {
        Tank tank = new Tank(new GridPoint2(1, 1));
        GameField field = new GameField(tank);
        field.movePlayer(Direction.RIGHT);

        field.update(0.4f);

        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(2, 1), tank.getCoordinates());
    }

    @Test
    void exposedTreeListCannotBeModifiedFromOutside() {
        GameField field = new GameField(new Tank(new GridPoint2(0, 0)));
        field.addTree(new Tree(new GridPoint2(2, 2)));

        assertThrows(UnsupportedOperationException.class,
                () -> field.getTrees().add(new Tree(new GridPoint2(3, 3))));
    }
}
