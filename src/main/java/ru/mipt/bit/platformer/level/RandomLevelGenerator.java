package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomLevelGenerator implements LevelLoader {

    private final int width;
    private final int height;
    private final int treeCount;
    private final Random random;

    public RandomLevelGenerator(int width, int height, int treeCount, Random random) {
        if (treeCount < 0 || treeCount >= width * height) {
            throw new IllegalArgumentException("Too many trees for " + width + "x" + height + " level: " + treeCount);
        }
        this.width = width;
        this.height = height;
        this.treeCount = treeCount;
        this.random = random;
    }

    @Override
    public LevelLayout load() {
        List<GridPoint2> cells = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells.add(new GridPoint2(x, y));
            }
        }
        Collections.shuffle(cells, random);

        // first cell goes to the player, next ones to trees - so they never overlap
        GridPoint2 playerStart = cells.get(0);
        List<GridPoint2> trees = cells.subList(1, treeCount + 1);
        return new LevelLayout(playerStart, trees);
    }
}
