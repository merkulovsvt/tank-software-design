package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.model.FieldObject;
import ru.mipt.bit.platformer.model.GameFieldListener;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.LivingObject;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class GameRenderer implements Disposable, GameFieldListener {

    private final Batch batch;
    private final TiledMap tiledMap;
    private final MapRenderer tiledMapRenderer;
    private final TileMovement tileMovement;
    private final HealthBarVisibility healthBarVisibility;
    private final Texture pixel;

    private final Map<Class<? extends GameObject>, String> texturePaths = new HashMap<>();
    private final Map<String, Texture> textures = new HashMap<>();
    private final Map<GameObject, ObjectGraphics> sprites = new LinkedHashMap<>();

    public GameRenderer(String levelFile, int levelWidth, int levelHeight, HealthBarVisibility healthBarVisibility) {
        this.healthBarVisibility = healthBarVisibility;
        batch = new SpriteBatch();
        pixel = createPixel();

        tiledMap = new TmxMapLoader().load(levelFile);
        tiledMapRenderer = createSingleLayerMapRenderer(tiledMap, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(tiledMap);
        if (groundLayer.getWidth() != levelWidth || groundLayer.getHeight() != levelHeight) {
            throw new IllegalArgumentException("Level is " + levelWidth + "x" + levelHeight + ", but map "
                    + levelFile + " is " + groundLayer.getWidth() + "x" + groundLayer.getHeight());
        }
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
    }

    public GameRenderer bindTexture(Class<? extends GameObject> type, String texturePath) {
        texturePaths.put(type, texturePath);
        return this;
    }

    @Override
    public void objectAdded(FieldObject object) {
        String texturePath = texturePaths.get(object.getClass());
        if (texturePath == null) {
            throw new IllegalStateException("No texture bound for " + object.getClass().getSimpleName());
        }
        Texture texture = textures.computeIfAbsent(texturePath, Texture::new);
        ObjectGraphics graphics = new SpriteRenderer(object, new TextureRegion(texture));
        if (object instanceof LivingObject) {
            graphics = new HealthBarDecorator(graphics, (LivingObject) object, healthBarVisibility, pixel);
        }
        sprites.put(object, graphics);
    }

    @Override
    public void objectRemoved(FieldObject object) {
        sprites.remove(object);
    }

    public void render() {
        clearScreen();

        // render each tile of the level
        tiledMapRenderer.render();

        // start recording all drawing commands
        batch.begin();

        for (ObjectGraphics graphics : sprites.values()) {
            graphics.draw(batch, tileMovement);
        }

        // submit all drawing requests
        batch.end();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    private static Texture createPixel() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    @Override
    public void dispose() {
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        for (Texture texture : textures.values()) {
            texture.dispose();
        }
        pixel.dispose();
        tiledMap.dispose();
        batch.dispose();
    }
}
