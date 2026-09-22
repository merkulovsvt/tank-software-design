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
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

/**
 * Owns everything libGDX: the tiled map, the sprite batch and the object textures.
 * Given a {@link GameField} it knows how to draw it; the model classes stay rendering-free.
 */
public class GameRenderer implements Disposable {

    private static final String TANK_TEXTURE = "images/tank_blue.png";
    private static final String TREE_TEXTURE = "images/greenTree.png";

    private final Batch batch;
    private final TiledMap tiledMap;
    private final MapRenderer tiledMapRenderer;
    private final TileMovement tileMovement;

    private final Texture tankTexture;
    private final Texture treeTexture;

    private final SpriteRenderer tankSprite;
    private final Map<Tree, SpriteRenderer> treeSprites = new LinkedHashMap<>();

    public GameRenderer(String levelFile, GameField field) {
        batch = new SpriteBatch();

        tiledMap = new TmxMapLoader().load(levelFile);
        tiledMapRenderer = createSingleLayerMapRenderer(tiledMap, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(tiledMap);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        tankTexture = new Texture(TANK_TEXTURE);
        treeTexture = new Texture(TREE_TEXTURE);

        tankSprite = new SpriteRenderer(new TextureRegion(tankTexture));
        for (Tree tree : field.getTrees()) {
            treeSprites.put(tree, new SpriteRenderer(new TextureRegion(treeTexture)));
        }
    }

    public void render(GameField field) {
        clearScreen();

        // render each tile of the level
        tiledMapRenderer.render();

        // start recording all drawing commands
        batch.begin();

        Tank player = field.getPlayer();
        tankSprite.draw(batch, tileMovement,
                player.getCoordinates(), player.getDestinationCoordinates(),
                player.getMovementProgress(), player.getRotation());

        for (Map.Entry<Tree, SpriteRenderer> entry : treeSprites.entrySet()) {
            GridPoint2 at = entry.getKey().getCoordinates();
            // a static object sits on a single tile: source == destination, progress 1, no rotation
            entry.getValue().draw(batch, tileMovement, at, at, 1f, 0f);
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
        treeTexture.dispose();
        tankTexture.dispose();
        tiledMap.dispose();
        batch.dispose();
    }
}
