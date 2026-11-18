package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.List;

public class GameField implements Battlefield {

    private final int width;
    private final int height;
    private final List<FieldObject> objects = new ArrayList<>();
    private final List<GameFieldListener> listeners = new ArrayList<>();

    public GameField(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void addListener(GameFieldListener listener) {
        listeners.add(listener);
    }

    @Override
    public void add(FieldObject object) {
        objects.add(object);
        for (GameFieldListener listener : listeners) {
            listener.objectAdded(object);
        }
    }

    @Override
    public void remove(FieldObject object) {
        if (!objects.remove(object)) {
            return;
        }
        for (GameFieldListener listener : listeners) {
            listener.objectRemoved(object);
        }
    }

    public void update(float deltaTime) {
        for (FieldObject object : new ArrayList<>(objects)) {
            if (!object.isDestroyed()) {
                object.update(deltaTime);
            }
        }
    }

    @Override
    public void hit(GridPoint2 coordinates, int damage) {
        for (FieldObject object : new ArrayList<>(objects)) {
            if (object.occupies(coordinates)) {
                object.hit(damage);
                if (object.isDestroyed()) {
                    remove(object);
                }
            }
        }
    }

    @Override
    public boolean isFree(GridPoint2 coordinates) {
        if (!isInside(coordinates)) {
            return false;
        }
        for (FieldObject object : objects) {
            if (object.occupies(coordinates)) {
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
