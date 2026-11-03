package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public interface Battlefield extends Obstacles {

    void add(FieldObject object);

    void remove(FieldObject object);

    void hit(GridPoint2 coordinates, int damage);
}
