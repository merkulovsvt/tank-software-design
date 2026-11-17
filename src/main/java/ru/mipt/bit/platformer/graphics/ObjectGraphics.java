package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.util.TileMovement;

/**
 * Infrastructure: drawing of one object inside the rendering adapter, not a port of the application.
 */
public interface ObjectGraphics {

    void draw(Batch batch, TileMovement tileMovement);
}
