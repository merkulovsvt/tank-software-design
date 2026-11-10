package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.LivingObject;
import ru.mipt.bit.platformer.util.TileMovement;

public class HealthBarDecorator implements ObjectGraphics {

    private static final float BAR_WIDTH = 100f;
    private static final float BAR_HEIGHT = 10f;
    private static final float BAR_OFFSET_Y = 54f;

    private final ObjectGraphics wrapped;
    private final LivingObject object;
    private final HealthBarVisibility visibility;
    private final Texture pixel;
    private final Rectangle rectangle = new Rectangle(0f, 0f, BAR_WIDTH, BAR_HEIGHT);

    public HealthBarDecorator(ObjectGraphics wrapped, LivingObject object, HealthBarVisibility visibility, Texture pixel) {
        this.wrapped = wrapped;
        this.object = object;
        this.visibility = visibility;
        this.pixel = pixel;
    }

    @Override
    public void draw(Batch batch, TileMovement tileMovement) {
        wrapped.draw(batch, tileMovement);
        if (!visibility.isVisible()) {
            return;
        }
        tileMovement.moveRectangleBetweenTileCenters(rectangle,
                object.getCoordinates(), object.getDestinationCoordinates(), object.getMovementProgress());
        float x = rectangle.x;
        float y = rectangle.y + BAR_OFFSET_Y;
        float healthShare = Math.max(0f, (float) object.getHealth() / object.getMaxHealth());

        batch.setColor(Color.RED);
        batch.draw(pixel, x, y, BAR_WIDTH, BAR_HEIGHT);
        batch.setColor(Color.GREEN);
        batch.draw(pixel, x, y, BAR_WIDTH * healthShare, BAR_HEIGHT);
        batch.setColor(Color.WHITE);
    }
}
