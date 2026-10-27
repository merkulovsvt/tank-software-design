package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameFieldTest {

    @Test
    void tankMovesIntoAFreeTile() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        GameField field = new GameField(10, 8);
        field.addTank(tank);

        tank.move(Direction.RIGHT, field);

        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(2, 1), tank.getDestinationCoordinates());
    }

    @Test
    void tankIntoATreeOnlyTurns() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        GameField field = new GameField(10, 8);
        field.addTank(tank);
        field.addObstacle(new Tree(new GridPoint2(1, 2)));

        tank.move(Direction.UP, field);

        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(Direction.UP.getRotation(), tank.getRotation());
    }

    @Test
    void isFreeIsFalseOnTreesAndTrueElsewhere() {
        GameField field = new GameField(10, 8);
        field.addObstacle(new Tree(new GridPoint2(2, 2)));

        assertFalse(field.isFree(new GridPoint2(2, 2)));
        assertTrue(field.isFree(new GridPoint2(3, 3)));
    }

    @Test
    void tilesOutsideTheLevelAreNotFree() {
        GameField field = new GameField(10, 8);

        assertTrue(field.isFree(new GridPoint2(0, 0)));
        assertTrue(field.isFree(new GridPoint2(9, 7)));
        assertFalse(field.isFree(new GridPoint2(-1, 0)));
        assertFalse(field.isFree(new GridPoint2(0, -1)));
        assertFalse(field.isFree(new GridPoint2(10, 0)));
        assertFalse(field.isFree(new GridPoint2(0, 8)));
    }

    @Test
    void tankAtTheBorderOnlyTurns() {
        Tank tank = new Tank(new GridPoint2(0, 7), 0.4f);
        GameField field = new GameField(10, 8);
        field.addTank(tank);

        tank.move(Direction.LEFT, field);
        assertFalse(tank.isMoving());
        assertEquals(Direction.LEFT.getRotation(), tank.getRotation());

        tank.move(Direction.UP, field);
        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(0, 7), tank.getCoordinates());
    }

    @Test
    void tankDoesNotDriveIntoAnotherTank() {
        Tank first = new Tank(new GridPoint2(1, 1), 0.4f);
        Tank second = new Tank(new GridPoint2(2, 1), 0.4f);
        GameField field = new GameField(10, 8);
        field.addTank(first);
        field.addTank(second);

        first.move(Direction.RIGHT, field);

        assertFalse(first.isMoving());
    }

    @Test
    void movingTankBlocksBothItsTiles() {
        Tank moving = new Tank(new GridPoint2(1, 1), 0.4f);
        GameField field = new GameField(10, 8);
        field.addTank(moving);
        moving.move(Direction.RIGHT, field);
        field.update(0.2f); // halfway between (1, 1) and (2, 1)

        assertFalse(field.isFree(new GridPoint2(1, 1)));
        assertFalse(field.isFree(new GridPoint2(2, 1)));

        // neither the tile it leaves nor the one it goes to can be taken by another tank
        Tank fromAbove = new Tank(new GridPoint2(1, 2), 0.4f);
        Tank fromRight = new Tank(new GridPoint2(3, 1), 0.4f);
        field.addTank(fromAbove);
        field.addTank(fromRight);
        fromAbove.move(Direction.DOWN, field);
        fromRight.move(Direction.LEFT, field);
        assertFalse(fromAbove.isMoving());
        assertFalse(fromRight.isMoving());
    }

    @Test
    void tileIsReleasedWhenTheTankArrives() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        GameField field = new GameField(10, 8);
        field.addTank(tank);
        tank.move(Direction.RIGHT, field);

        field.update(0.4f);

        assertTrue(field.isFree(new GridPoint2(1, 1)));
        assertFalse(field.isFree(new GridPoint2(2, 1)));
    }

    @Test
    void updateAdvancesAllTanks() {
        Tank first = new Tank(new GridPoint2(1, 1), 0.4f);
        Tank second = new Tank(new GridPoint2(5, 5), 0.4f);
        GameField field = new GameField(10, 8);
        field.addTank(first);
        field.addTank(second);
        first.move(Direction.RIGHT, field);
        second.move(Direction.UP, field);

        field.update(0.4f);

        assertEquals(new GridPoint2(2, 1), first.getCoordinates());
        assertEquals(new GridPoint2(5, 6), second.getCoordinates());
    }
}
