package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileLevelLoaderTest {

    @Test
    void parseFindsTreesAndPlayerCountingRowsFromTheBottom() {
        LevelLayout layout = FileLevelLoader.parse(List.of(
                "T__",
                "_X_",
                "__T"
        ));

        assertEquals(new GridPoint2(1, 1), layout.getPlayerStart());
        assertEquals(List.of(new GridPoint2(0, 2), new GridPoint2(2, 0)), layout.getTreeCoordinates());
    }

    @Test
    void parseWithoutPlayerFails() {
        assertThrows(IllegalArgumentException.class, () -> FileLevelLoader.parse(List.of("T__", "___")));
    }

    @Test
    void parseWithTwoPlayersFails() {
        assertThrows(IllegalArgumentException.class, () -> FileLevelLoader.parse(List.of("X__", "__X")));
    }

    @Test
    void parseWithUnknownCellFails() {
        assertThrows(IllegalArgumentException.class, () -> FileLevelLoader.parse(List.of("X_#")));
    }

    @Test
    void loadReadsLevelFromResources() {
        LevelLayout layout = new FileLevelLoader("levels/level1.txt").load();

        assertEquals(new GridPoint2(5, 2), layout.getPlayerStart());
        assertTrue(layout.getTreeCoordinates().contains(new GridPoint2(3, 6)));
        assertEquals(15, layout.getTreeCoordinates().size());
    }

    @Test
    void loadOfMissingFileFails() {
        assertThrows(IllegalArgumentException.class, () -> new FileLevelLoader("levels/missing.txt").load());
    }
}
