package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.util.TileMovement;

public interface ObjectGraphics {

    void draw(Batch batch, TileMovement tileMovement);
}
