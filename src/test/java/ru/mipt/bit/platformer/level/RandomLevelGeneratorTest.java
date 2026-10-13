package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomLevelGeneratorTest {

    @Test
    void generatesRequestedNumberOfTreesInsideTheLevel() {
        LevelLayout layout = new RandomLevelGenerator(10, 8, 20, new Random(42)).load();

        assertEquals(20, layout.getTreeCoordinates().size());
        for (GridPoint2 tree : layout.getTreeCoordinates()) {
            assertTrue(tree.x >= 0 && tree.x < 10 && tree.y >= 0 && tree.y < 8);
        }
    }

    @Test
    void treesDoNotOverlapEachOtherAndThePlayer() {
        for (int seed = 0; seed < 100; seed++) {
            LevelLayout layout = new RandomLevelGenerator(4, 4, 15, new Random(seed)).load();

            Set<GridPoint2> trees = new HashSet<>(layout.getTreeCoordinates());
            assertEquals(15, trees.size());
            assertFalse(trees.contains(layout.getPlayerStart()));
        }
    }

    @Test
    void sameSeedGivesSameLevel() {
        LevelLayout first = new RandomLevelGenerator(10, 8, 10, new Random(7)).load();
        LevelLayout second = new RandomLevelGenerator(10, 8, 10, new Random(7)).load();

        assertEquals(first.getPlayerStart(), second.getPlayerStart());
        assertEquals(first.getTreeCoordinates(), second.getTreeCoordinates());
    }

    @Test
    void tooManyTreesFail() {
        assertThrows(IllegalArgumentException.class, () -> new RandomLevelGenerator(2, 2, 4, new Random()));
    }
}
