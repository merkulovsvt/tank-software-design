package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LevelLayout {

    private final int width;
    private final int height;
    private final GridPoint2 playerStart;
    private final List<GridPoint2> treeCoordinates;

    public LevelLayout(int width, int height, GridPoint2 playerStart, List<GridPoint2> treeCoordinates) {
        this.width = width;
        this.height = height;
        this.playerStart = new GridPoint2(playerStart);
        this.treeCoordinates = new ArrayList<>();
        for (GridPoint2 coordinates : treeCoordinates) {
            this.treeCoordinates.add(new GridPoint2(coordinates));
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public GridPoint2 getPlayerStart() {
        return new GridPoint2(playerStart);
    }

    public List<GridPoint2> getTreeCoordinates() {
        List<GridPoint2> copies = new ArrayList<>();
        for (GridPoint2 coordinates : treeCoordinates) {
            copies.add(new GridPoint2(coordinates));
        }
        return Collections.unmodifiableList(copies);
    }
}
