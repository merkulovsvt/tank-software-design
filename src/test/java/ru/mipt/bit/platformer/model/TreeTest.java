package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TreeTest {

    @Test
    void keepsItsCoordinates() {
        Tree tree = new Tree(new GridPoint2(2, 7));
        assertEquals(new GridPoint2(2, 7), tree.getCoordinates());
    }

    @Test
    void occupiesOnlyItsOwnTile() {
        Tree tree = new Tree(new GridPoint2(2, 7));
        assertTrue(tree.occupies(new GridPoint2(2, 7)));
        assertFalse(tree.occupies(new GridPoint2(2, 8)));
    }

    @Test
    void copiesTheGivenPointSoLaterMutationsDoNotLeakIn() {
        GridPoint2 start = new GridPoint2(2, 7);
        Tree tree = new Tree(start);
        start.set(0, 0);
        assertEquals(new GridPoint2(2, 7), tree.getCoordinates());
    }
}
