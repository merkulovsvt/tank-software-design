package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectionTest {

    @Test
    void nextCoordinatesReturnsNeighbourForEachDirection() {
        GridPoint2 from = new GridPoint2(3, 5);
        assertEquals(new GridPoint2(3, 6), Direction.UP.nextCoordinates(from));
        assertEquals(new GridPoint2(2, 5), Direction.LEFT.nextCoordinates(from));
        assertEquals(new GridPoint2(3, 4), Direction.DOWN.nextCoordinates(from));
        assertEquals(new GridPoint2(4, 5), Direction.RIGHT.nextCoordinates(from));
    }

    @Test
    void nextCoordinatesDoesNotMutateItsArgument() {
        GridPoint2 from = new GridPoint2(3, 5);
        Direction.UP.nextCoordinates(from);
        assertEquals(new GridPoint2(3, 5), from);
    }

    @Test
    void rotationMatchesTheSpriteOrientation() {
        assertEquals(90f, Direction.UP.getRotation());
        assertEquals(-180f, Direction.LEFT.getRotation());
        assertEquals(-90f, Direction.DOWN.getRotation());
        assertEquals(0f, Direction.RIGHT.getRotation());
    }
}
