package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankTest {

    private static final Obstacles ALL_FREE = coordinates -> true;
    private static final Obstacles ALL_BLOCKED = coordinates -> false;

    @Test
    void startsAtRestFacingRight() {
        Tank tank = new Tank(new GridPoint2(1, 1));
        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
        assertEquals(1f, tank.getMovementProgress());
        assertEquals(0f, tank.getRotation());
    }

    @Test
    void copiesItsStartPointSoLaterMutationsDoNotLeakIn() {
        GridPoint2 start = new GridPoint2(1, 1);
        Tank tank = new Tank(start);
        start.set(9, 9);
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
    }

    @Test
    void moveIntoAFreeTileStartsMovingAndTurns() {
        Tank tank = new Tank(new GridPoint2(1, 1));
        tank.move(Direction.UP, ALL_FREE);
        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());       // not there yet
        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getMovementProgress());
        assertEquals(Direction.UP.getRotation(), tank.getRotation());
    }

    @Test
    void moveIntoABlockedTileOnlyTurns() {
        Tank tank = new Tank(new GridPoint2(1, 1));
        tank.move(Direction.LEFT, ALL_BLOCKED);
        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
        assertEquals(Direction.LEFT.getRotation(), tank.getRotation());
    }

    @Test
    void aStartedMoveIsNotInterrupted() {
        Tank tank = new Tank(new GridPoint2(1, 1));
        tank.move(Direction.RIGHT, ALL_FREE); // heading right
        tank.move(Direction.UP, ALL_FREE);    // ignored while moving
        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(2, 1), tank.getDestinationCoordinates());
        assertEquals(Direction.RIGHT.getRotation(), tank.getRotation());
    }

    @Test
    void updateAdvancesProgressAndArrivesAfterOneTileTime() {
        Tank tank = new Tank(new GridPoint2(1, 1));
        tank.move(Direction.RIGHT, ALL_FREE);

        tank.update(0.2f); // halfway (speed 0.4s per tile)
        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());

        tank.update(0.2f); // arrived
        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(2, 1), tank.getCoordinates());
        assertEquals(1f, tank.getMovementProgress());
    }
}
