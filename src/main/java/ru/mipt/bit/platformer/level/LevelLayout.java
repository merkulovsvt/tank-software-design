package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LevelLayout {

    private final GridPoint2 playerStart;
    private final List<GridPoint2> treeCoordinates;

    public LevelLayout(GridPoint2 playerStart, List<GridPoint2> treeCoordinates) {
        this.playerStart = new GridPoint2(playerStart);
        this.treeCoordinates = new ArrayList<>();
        for (GridPoint2 coordinates : treeCoordinates) {
            this.treeCoordinates.add(new GridPoint2(coordinates));
        }
    }

    public GridPoint2 getPlayerStart() {
        return new GridPoint2(playerStart);
    }

    public List<GridPoint2> getTreeCoordinates() {
        return Collections.unmodifiableList(treeCoordinates);
    }
}
