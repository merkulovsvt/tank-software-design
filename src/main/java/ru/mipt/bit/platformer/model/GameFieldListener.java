package ru.mipt.bit.platformer.model;

/**
 * Driven port (domain): the field tells the outside world that objects appeared or disappeared.
 * Adapter: graphics.GameRenderer.
 */
public interface GameFieldListener {

    void objectAdded(FieldObject object);

    void objectRemoved(FieldObject object);
}
