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
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
        assertEquals(1f, tank.getMovementProgress());
        assertEquals(0f, tank.getRotation());
    }

    @Test
    void copiesItsStartPointSoLaterMutationsDoNotLeakIn() {
        GridPoint2 start = new GridPoint2(1, 1);
        Tank tank = new Tank(start, 0.4f);
        start.set(9, 9);
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
    }

    @Test
    void returnedPointsCannotChangeTheTank() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        tank.getCoordinates().set(9, 9);
        tank.getDestinationCoordinates().set(9, 9);
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
    }

    @Test
    void moveIntoAFreeTileStartsMovingAndTurns() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        tank.move(Direction.UP, ALL_FREE);
        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());       // not there yet
        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getMovementProgress());
        assertEquals(Direction.UP.getRotation(), tank.getRotation());
    }

    @Test
    void moveIntoABlockedTileOnlyTurns() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        tank.move(Direction.LEFT, ALL_BLOCKED);
        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
        assertEquals(Direction.LEFT.getRotation(), tank.getRotation());
    }

    @Test
    void aStartedMoveIsNotInterrupted() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        tank.move(Direction.RIGHT, ALL_FREE); // heading right
        tank.move(Direction.UP, ALL_FREE);    // ignored while moving
        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(2, 1), tank.getDestinationCoordinates());
        assertEquals(Direction.RIGHT.getRotation(), tank.getRotation());
    }

    @Test
    void restingTankOccupiesOnlyItsTile() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        assertTrue(tank.occupies(new GridPoint2(1, 1)));
        assertFalse(tank.occupies(new GridPoint2(2, 1)));
    }

    @Test
    void movingTankOccupiesBothTiles() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        tank.move(Direction.RIGHT, ALL_FREE);
        assertTrue(tank.occupies(new GridPoint2(1, 1)));
        assertTrue(tank.occupies(new GridPoint2(2, 1)));
        assertFalse(tank.occupies(new GridPoint2(0, 1)));

        tank.update(0.4f);
        assertFalse(tank.occupies(new GridPoint2(1, 1)));
        assertTrue(tank.occupies(new GridPoint2(2, 1)));
    }

    @Test
    void updateAdvancesProgressAndArrivesAfterOneTileTime() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        tank.move(Direction.RIGHT, ALL_FREE);

        tank.update(0.2f); // halfway (speed 0.4s per tile)
        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());

        tank.update(0.2f); // arrived
        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(2, 1), tank.getCoordinates());
        assertEquals(1f, tank.getMovementProgress());
    }

    @Test
    void shootPutsABulletInFrontOfTheTankFlyingWhereItLooks() {
        GameField field = new GameField(10, 8);
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        field.add(tank);
        tank.move(Direction.UP, ALL_BLOCKED);

        tank.shoot(field);

        assertFalse(field.isFree(new GridPoint2(1, 2)));
        field.update(0f);
        assertFalse(field.isFree(new GridPoint2(1, 3)));
    }

    private static class AddedCounter implements GameFieldListener {
        int added;

        @Override
        public void objectAdded(FieldObject object) {
            added++;
        }

        @Override
        public void objectRemoved(FieldObject object) {
        }
    }

    @Test
    void cannotShootWhileMoving() {
        GameField field = new GameField(10, 8);
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        field.add(tank);
        tank.move(Direction.RIGHT, field);
        AddedCounter counter = new AddedCounter();
        field.addListener(counter);

        tank.shoot(field);

        assertEquals(0, counter.added);
    }

    @Test
    void secondShotWaitsForReload() {
        GameField field = new GameField(10, 8);
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        Tank target = new Tank(new GridPoint2(2, 1), 0.4f);
        field.add(tank);
        field.add(target);

        tank.shoot(field);
        tank.shoot(field);
        assertEquals(75, target.getHealth());

        field.update(0.5f);
        tank.shoot(field);
        assertEquals(50, target.getHealth());
    }

    @Test
    void pointBlankShotHitsTheNeighbourWithoutABullet() {
        GameField field = new GameField(10, 8);
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        Tank target = new Tank(new GridPoint2(2, 1), 0.4f);
        field.add(tank);
        field.add(target);

        tank.shoot(field);

        assertEquals(75, target.getHealth());
        assertFalse(tank.isDestroyed());
        assertEquals(100, tank.getHealth());
    }

    @Test
    void shotIntoATreeOrTheEdgeChangesNothing() {
        GameField field = new GameField(10, 8);
        Tank tank = new Tank(new GridPoint2(9, 1), 0.4f);
        Tree tree = new Tree(new GridPoint2(9, 2));
        field.add(tank);
        field.add(tree);
        AddedCounter counter = new AddedCounter();
        field.addListener(counter);

        tank.shoot(field);
        field.update(0.5f);
        tank.move(Direction.UP, field);
        tank.shoot(field);

        assertEquals(0, counter.added);
        assertEquals(100, tank.getHealth());
        assertFalse(field.isFree(new GridPoint2(9, 2)));
    }

    @Test
    void startsWithFullHealth() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        assertEquals(100, tank.getMaxHealth());
        assertEquals(tank.getMaxHealth(), tank.getHealth());
    }

    @Test
    void tankIsDestroyedWhenHealthRunsOut() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);

        tank.hit(75);
        assertFalse(tank.isDestroyed());
        tank.hit(25);
        assertTrue(tank.isDestroyed());
    }

    @Test
    void destroyedTankNeitherMovesNorShoots() {
        GameField field = new GameField(10, 8);
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        tank.hit(100);

        AddedCounter counter = new AddedCounter();
        field.addListener(counter);

        tank.move(Direction.UP, ALL_FREE);
        tank.shoot(field);

        assertFalse(tank.isMoving());
        assertEquals(0, counter.added);
    }
}
