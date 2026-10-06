package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class SpriteRenderer {

    private final TextureRegion region;
    private final Rectangle rectangle;

    public SpriteRenderer(TextureRegion region) {
        this.region = region;
        this.rectangle = createBoundingRectangle(region);
    }

    public void draw(Batch batch, TileMovement tileMovement,
                     GridPoint2 from, GridPoint2 to, float movementProgress, float rotation) {
        tileMovement.moveRectangleBetweenTileCenters(rectangle, from, to, movementProgress);
        drawTextureRegionUnscaled(batch, region, rectangle, rotation);
    }
}
