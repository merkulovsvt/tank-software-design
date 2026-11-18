package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class SpriteRenderer implements ObjectGraphics {

    private final GameObject object;
    private final TextureRegion region;
    private final Rectangle rectangle;

    public SpriteRenderer(GameObject object, TextureRegion region) {
        this.object = object;
        this.region = region;
        this.rectangle = createBoundingRectangle(region);
    }

    @Override
    public void draw(Batch batch, TileMovement tileMovement) {
        tileMovement.moveRectangleBetweenTileCenters(rectangle,
                object.getCoordinates(), object.getDestinationCoordinates(), object.getMovementProgress());
        drawTextureRegionUnscaled(batch, region, rectangle, object.getRotation());
    }
}
