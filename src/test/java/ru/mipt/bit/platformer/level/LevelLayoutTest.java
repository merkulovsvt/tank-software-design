package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LevelLayoutTest {

    @Test
    void returnedTreePointsCannotChangeTheLayout() {
        LevelLayout layout = new LevelLayout(10, 8, new GridPoint2(0, 0), List.of(new GridPoint2(2, 3)));

        layout.getTreeCoordinates().get(0).set(9, 9);

        assertEquals(List.of(new GridPoint2(2, 3)), layout.getTreeCoordinates());
    }

    @Test
    void treeListCannotBeModified() {
        LevelLayout layout = new LevelLayout(10, 8, new GridPoint2(0, 0), List.of(new GridPoint2(2, 3)));

        assertThrows(UnsupportedOperationException.class,
                () -> layout.getTreeCoordinates().add(new GridPoint2(4, 4)));
    }

    @Test
    void copiesGivenPointsSoLaterMutationsDoNotLeakIn() {
        GridPoint2 start = new GridPoint2(0, 0);
        List<GridPoint2> trees = new ArrayList<>(List.of(new GridPoint2(2, 3)));
        LevelLayout layout = new LevelLayout(10, 8, start, trees);

        start.set(5, 5);
        trees.get(0).set(9, 9);
        trees.add(new GridPoint2(4, 4));

        assertEquals(new GridPoint2(0, 0), layout.getPlayerStart());
        assertEquals(List.of(new GridPoint2(2, 3)), layout.getTreeCoordinates());
    }
}
