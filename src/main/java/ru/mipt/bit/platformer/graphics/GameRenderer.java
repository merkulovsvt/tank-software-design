package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
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

    private final Map<Class<? extends GameObject>, String> texturePaths = new HashMap<>();
    private final Map<String, Texture> textures = new HashMap<>();
    private final Map<GameObject, SpriteRenderer> sprites = new LinkedHashMap<>();

    public GameRenderer(String levelFile, int levelWidth, int levelHeight) {
        batch = new SpriteBatch();

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
        sprites.put(object, new SpriteRenderer(new TextureRegion(texture)));
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

        for (Map.Entry<GameObject, SpriteRenderer> entry : sprites.entrySet()) {
            GameObject object = entry.getKey();
            entry.getValue().draw(batch, tileMovement,
                    object.getCoordinates(), object.getDestinationCoordinates(),
                    object.getMovementProgress(), object.getRotation());
        }

        // submit all drawing requests
        batch.end();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void dispose() {
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        for (Texture texture : textures.values()) {
            texture.dispose();
        }
        tiledMap.dispose();
        batch.dispose();
    }
}
