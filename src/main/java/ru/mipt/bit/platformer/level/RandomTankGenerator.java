package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomTankGenerator {

    private final int width;
    private final int height;
    private final int tankCount;
    private final Random random;

    public RandomTankGenerator(int width, int height, int tankCount, Random random) {
        if (tankCount < 0) {
            throw new IllegalArgumentException("Negative tank count: " + tankCount);
        }
        this.width = width;
        this.height = height;
        this.tankCount = tankCount;
        this.random = random;
    }

    // picks start tiles for enemy tanks among the tiles not taken by the player and trees
    public List<GridPoint2> generate(LevelLayout layout) {
        List<GridPoint2> freeCells = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                GridPoint2 cell = new GridPoint2(x, y);
                if (!cell.equals(layout.getPlayerStart()) && !layout.getTreeCoordinates().contains(cell)) {
                    freeCells.add(cell);
                }
            }
        }
        if (tankCount > freeCells.size()) {
            throw new IllegalArgumentException("Not enough free tiles for " + tankCount + " tanks");
        }
        Collections.shuffle(freeCells, random);
        return new ArrayList<>(freeCells.subList(0, tankCount));
    }
}
