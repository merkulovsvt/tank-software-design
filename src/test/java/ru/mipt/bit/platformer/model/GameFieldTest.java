package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameFieldTest {

    @Test
    void tankMovesIntoAFreeTile() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        GameField field = new GameField(10, 8);
        field.add(tank);

        tank.move(Direction.RIGHT, field);

        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(2, 1), tank.getDestinationCoordinates());
    }

    @Test
    void tankIntoATreeOnlyTurns() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        GameField field = new GameField(10, 8);
        field.add(tank);
        field.add(new Tree(new GridPoint2(1, 2)));

        tank.move(Direction.UP, field);

        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(Direction.UP.getRotation(), tank.getRotation());
    }

    @Test
    void isFreeIsFalseOnTreesAndTrueElsewhere() {
        GameField field = new GameField(10, 8);
        field.add(new Tree(new GridPoint2(2, 2)));

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
        field.add(tank);

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
        field.add(first);
        field.add(second);

        first.move(Direction.RIGHT, field);

        assertFalse(first.isMoving());
    }

    @Test
    void movingTankBlocksBothItsTiles() {
        Tank moving = new Tank(new GridPoint2(1, 1), 0.4f);
        GameField field = new GameField(10, 8);
        field.add(moving);
        moving.move(Direction.RIGHT, field);
        field.update(0.2f); // halfway between (1, 1) and (2, 1)

        assertFalse(field.isFree(new GridPoint2(1, 1)));
        assertFalse(field.isFree(new GridPoint2(2, 1)));

        // neither the tile it leaves nor the one it goes to can be taken by another tank
        Tank fromAbove = new Tank(new GridPoint2(1, 2), 0.4f);
        Tank fromRight = new Tank(new GridPoint2(3, 1), 0.4f);
        field.add(fromAbove);
        field.add(fromRight);
        fromAbove.move(Direction.DOWN, field);
        fromRight.move(Direction.LEFT, field);
        assertFalse(fromAbove.isMoving());
        assertFalse(fromRight.isMoving());
    }

    @Test
    void tileIsReleasedWhenTheTankArrives() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        GameField field = new GameField(10, 8);
        field.add(tank);
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
        field.add(first);
        field.add(second);
        first.move(Direction.RIGHT, field);
        second.move(Direction.UP, field);

        field.update(0.4f);

        assertEquals(new GridPoint2(2, 1), first.getCoordinates());
        assertEquals(new GridPoint2(5, 6), second.getCoordinates());
    }

    private static class RecordingListener implements GameFieldListener {
        final List<String> events = new ArrayList<>();

        @Override
        public void objectAdded(FieldObject object) {
            events.add("added " + object.getClass().getSimpleName());
        }

        @Override
        public void objectRemoved(FieldObject object) {
            events.add("removed " + object.getClass().getSimpleName());
        }
    }

    @Test
    void listenersLearnAboutAddedAndRemovedObjects() {
        GameField field = new GameField(10, 8);
        RecordingListener listener = new RecordingListener();
        field.addListener(listener);
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);

        field.add(tank);
        field.add(new Tree(new GridPoint2(3, 3)));
        field.remove(tank);
        field.remove(tank);

        assertEquals(List.of("added Tank", "added Tree", "removed Tank"), listener.events);
    }

    @Test
    void listenersLearnAboutBulletsDuringTheGame() {
        GameField field = new GameField(10, 8);
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        field.add(tank);
        RecordingListener listener = new RecordingListener();
        field.addListener(listener);

        tank.shoot(field);
        field.update(0f);
        for (int i = 0; i < 10; i++) {
            field.update(0.1f);
        }

        assertEquals(List.of("added Bullet", "removed Bullet"), listener.events);
    }

    @Test
    void hitDamagesWhatIsOnTheTileAndRemovesDestroyedTanks() {
        GameField field = new GameField(10, 8);
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        field.add(tank);
        RecordingListener listener = new RecordingListener();
        field.addListener(listener);

        field.hit(new GridPoint2(1, 1), 75);
        assertEquals(25, tank.getHealth());
        assertFalse(field.isFree(new GridPoint2(1, 1)));

        field.hit(new GridPoint2(1, 1), 25);
        assertTrue(tank.isDestroyed());
        assertTrue(field.isFree(new GridPoint2(1, 1)));
        assertEquals(List.of("removed Tank"), listener.events);
    }

    @Test
    void tankIsKilledByThreeShots() {
        GameField field = new GameField(10, 8);
        Tank shooter = new Tank(new GridPoint2(1, 1), 0.4f);
        Tank target = new Tank(new GridPoint2(5, 1), 0.4f);
        field.add(shooter);
        field.add(target);

        for (int i = 0; i < 100 && !target.isDestroyed(); i++) {
            shooter.shoot(field);
            field.update(0.1f);
        }

        assertTrue(target.isDestroyed());
        assertTrue(field.isFree(new GridPoint2(5, 1)));
        assertFalse(shooter.isDestroyed());
    }
}
