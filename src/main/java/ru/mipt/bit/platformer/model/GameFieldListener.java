package ru.mipt.bit.platformer.model;

public interface GameFieldListener {

    void objectAdded(FieldObject object);

    void objectRemoved(FieldObject object);
}
