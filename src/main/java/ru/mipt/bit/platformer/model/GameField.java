package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.List;

public class GameField implements Obstacles {

    private final Tank player;
    private final List<Obstacle> obstacles = new ArrayList<>();

    public GameField(Tank player) {
        this.player = player;
    }

    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    public void movePlayer(Direction direction) {
        player.move(direction, this);
    }

    public void update(float deltaTime) {
        player.update(deltaTime);
    }

    @Override
    public boolean isFree(GridPoint2 coordinates) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.occupies(coordinates)) {
                return false;
            }
        }
        return true;
    }
}
