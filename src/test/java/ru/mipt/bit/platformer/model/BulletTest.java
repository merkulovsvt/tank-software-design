package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BulletTest {

    private static final float SPEED = 0.1f;

    @Test
    void fliesTileByTileInItsDirection() {
        GameField field = new GameField(10, 8);
        Bullet bullet = new Bullet(new GridPoint2(1, 1), Direction.RIGHT, SPEED, 1, field);
        field.add(bullet);

        field.update(0f);
        assertEquals(new GridPoint2(2, 1), bullet.getDestinationCoordinates());

        field.update(SPEED);
        assertEquals(new GridPoint2(2, 1), bullet.getCoordinates());
        assertEquals(new GridPoint2(3, 1), bullet.getDestinationCoordinates());
        assertEquals(Direction.RIGHT.getRotation(), bullet.getRotation());
    }

    @Test
    void disappearsAtTheEdgeOfTheField() {
        GameField field = new GameField(10, 8);
        Bullet bullet = new Bullet(new GridPoint2(9, 1), Direction.RIGHT, SPEED, 1, field);
        field.add(bullet);

        field.update(0f);

        assertTrue(bullet.isDestroyed());
        assertTrue(field.isFree(new GridPoint2(9, 1)));
    }

    @Test
    void disappearsOnATreeAndTheTreeStays() {
        GameField field = new GameField(10, 8);
        field.add(new Tree(new GridPoint2(3, 1)));
        Bullet bullet = new Bullet(new GridPoint2(1, 1), Direction.RIGHT, SPEED, 1, field);
        field.add(bullet);

        field.update(0f);
        field.update(SPEED);

        assertTrue(bullet.isDestroyed());
        assertTrue(field.isFree(new GridPoint2(2, 1)));
        assertFalse(field.isFree(new GridPoint2(3, 1)));
    }

    @Test
    void damagesATankAndDisappears() {
        GameField field = new GameField(10, 8);
        Tank tank = new Tank(new GridPoint2(2, 1), 0.4f);
        field.add(tank);
        Bullet bullet = new Bullet(new GridPoint2(1, 1), Direction.RIGHT, SPEED, 1, field);
        field.add(bullet);

        field.update(0f);

        assertTrue(bullet.isDestroyed());
        assertEquals(2, tank.getHealth());
        assertTrue(field.isFree(new GridPoint2(1, 1)));
    }

    @Test
    void twoBulletsDestroyEachOther() {
        GameField field = new GameField(10, 8);
        Bullet right = new Bullet(new GridPoint2(1, 1), Direction.RIGHT, SPEED, 1, field);
        Bullet left = new Bullet(new GridPoint2(2, 1), Direction.LEFT, SPEED, 1, field);
        field.add(right);
        field.add(left);

        field.update(0f);

        assertTrue(right.isDestroyed());
        assertTrue(left.isDestroyed());
        assertTrue(field.isFree(new GridPoint2(1, 1)));
        assertTrue(field.isFree(new GridPoint2(2, 1)));
    }

    @Test
    void blocksTheTilesItFliesThrough() {
        GameField field = new GameField(10, 8);
        Bullet bullet = new Bullet(new GridPoint2(1, 1), Direction.RIGHT, SPEED, 1, field);
        field.add(bullet);
        field.update(0f);

        assertTrue(bullet.occupies(new GridPoint2(1, 1)));
        assertTrue(bullet.occupies(new GridPoint2(2, 1)));
        assertFalse(bullet.occupies(new GridPoint2(3, 1)));
    }
}
