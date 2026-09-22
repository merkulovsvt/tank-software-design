package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The playing field: the player tank, the obstacles standing on it and the collision rule.
 * It is the {@link Obstacles} a tank consults, so tanks and trees never reference each other.
 */
public class GameField implements Obstacles {

    private final Tank player;
    private final List<Tree> trees = new ArrayList<>();

    public GameField(Tank player) {
        this.player = player;
    }

    public void addTree(Tree tree) {
        trees.add(tree);
    }

    public void movePlayer(Direction direction) {
        player.move(direction, this);
    }

    public void update(float deltaTime) {
        player.update(deltaTime);
    }

    @Override
    public boolean isFree(GridPoint2 coordinates) {
        for (Tree tree : trees) {
            if (tree.occupies(coordinates)) {
                return false;
            }
        }
        return true;
    }

    public Tank getPlayer() {
        return player;
    }

    public List<Tree> getTrees() {
        return Collections.unmodifiableList(trees);
    }
}
