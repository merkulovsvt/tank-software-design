package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.List;

public class GameField implements Obstacles {

    private final Tank player;
    private final int width;
    private final int height;
    private final List<Obstacle> obstacles = new ArrayList<>();

    public GameField(Tank player, int width, int height) {
        this.player = player;
        this.width = width;
        this.height = height;
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
        if (!isInside(coordinates)) {
            return false;
        }
        for (Obstacle obstacle : obstacles) {
            if (obstacle.occupies(coordinates)) {
                return false;
            }
        }
        return true;
    }

    private boolean isInside(GridPoint2 coordinates) {
        return coordinates.x >= 0 && coordinates.x < width
                && coordinates.y >= 0 && coordinates.y < height;
    }
}
