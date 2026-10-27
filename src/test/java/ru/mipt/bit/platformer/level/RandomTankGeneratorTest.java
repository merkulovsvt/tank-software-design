package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomTankGeneratorTest {

    @Test
    void generatesRequestedNumberOfTanksInsideTheLevel() {
        LevelLayout layout = new LevelLayout(new GridPoint2(0, 0), List.of());

        List<GridPoint2> tanks = new RandomTankGenerator(10, 8, 5, new Random(42)).generate(layout);

        assertEquals(5, tanks.size());
        for (GridPoint2 tank : tanks) {
            assertTrue(tank.x >= 0 && tank.x < 10 && tank.y >= 0 && tank.y < 8);
        }
    }

    @Test
    void tanksDoNotOverlapEachOtherThePlayerAndTrees() {
        for (int seed = 0; seed < 100; seed++) {
            LevelLayout layout = new RandomLevelGenerator(4, 4, 8, new Random(seed)).load();

            List<GridPoint2> tanks = new RandomTankGenerator(4, 4, 7, new Random(seed)).generate(layout);

            Set<GridPoint2> unique = new HashSet<>(tanks);
            assertEquals(7, unique.size());
            assertFalse(unique.contains(layout.getPlayerStart()));
            for (GridPoint2 tree : layout.getTreeCoordinates()) {
                assertFalse(unique.contains(tree));
            }
        }
    }

    @Test
    void tooManyTanksFail() {
        LevelLayout layout = new LevelLayout(new GridPoint2(0, 0), List.of(new GridPoint2(1, 0)));

        assertThrows(IllegalArgumentException.class,
                () -> new RandomTankGenerator(2, 2, 3, new Random()).generate(layout));
    }

    @Test
    void negativeTankCountFails() {
        assertThrows(IllegalArgumentException.class, () -> new RandomTankGenerator(2, 2, -1, new Random()));
    }
}
