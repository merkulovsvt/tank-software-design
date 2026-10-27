package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.List;

public class GameField implements Obstacles {

    private final int width;
    private final int height;
    private final List<Tank> tanks = new ArrayList<>();
    private final List<Obstacle> obstacles = new ArrayList<>();

    public GameField(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    public void addTank(Tank tank) {
        tanks.add(tank);
        obstacles.add(tank);
    }

    public void update(float deltaTime) {
        for (Tank tank : tanks) {
            tank.update(deltaTime);
        }
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
