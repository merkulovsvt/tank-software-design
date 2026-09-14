package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
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
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.badlogic.gdx.Input.Keys.*;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameDesktopLauncher implements ApplicationListener {

    private Batch batch;

    private TiledMap level;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;

    private Texture blueTankTexture;
    private Texture greenTreeTexture;

    private GameField field;
    private PlayerInputController inputController;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // load level tiles
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        // Texture decodes an image file and loads it into GPU memory, it represents a native resource
        blueTankTexture = new Texture("images/tank_blue.png");
        greenTreeTexture = new Texture("images/greenTree.png");

        // TextureRegion represents Texture portion, there may be many TextureRegion instances of the same Texture
        Tank player = new Tank(new TextureRegion(blueTankTexture), new GridPoint2(1, 1));
        field = new GameField(player);
        field.addTree(new Tree(new TextureRegion(greenTreeTexture), new GridPoint2(1, 3), groundLayer));

        inputController = new PlayerInputController();
    }

    @Override
    public void render() {
        // clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();

        Direction pressedDirection = inputController.getPressedDirection();
        if (pressedDirection != null) {
            field.movePlayer(pressedDirection);
        }

        field.update(deltaTime, tileMovement);

        // render each tile of the level
        levelRenderer.render();

        // start recording all drawing commands
        batch.begin();

        field.draw(batch);

        // submit all drawing requests
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }

    @Override
    public void dispose() {
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        greenTreeTexture.dispose();
        blueTankTexture.dispose();
        level.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}

enum Direction {
    UP(0, 1, 90f),
    LEFT(-1, 0, -180f),
    DOWN(0, -1, -90f),
    RIGHT(1, 0, 0f);

    private final int shiftX;
    private final int shiftY;
    private final float rotation;

    Direction(int shiftX, int shiftY, float rotation) {
        this.shiftX = shiftX;
        this.shiftY = shiftY;
        this.rotation = rotation;
    }

    GridPoint2 nextCoordinates(GridPoint2 coordinates) {
        return new GridPoint2(coordinates).add(shiftX, shiftY);
    }

    float getRotation() {
        return rotation;
    }
}

interface Obstacles {

    boolean isFree(GridPoint2 coordinates);
}

class Tank {

    private static final float MOVEMENT_SPEED = 0.4f;

    private final TextureRegion graphics;
    private final Rectangle rectangle;
    // player current position coordinates on level 10x8 grid (e.g. x=0, y=1)
    private final GridPoint2 coordinates;
    // which tile the player want to go next
    private final GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private float rotation = 0f;

    Tank(TextureRegion graphics, GridPoint2 initialCoordinates) {
        this.graphics = graphics;
        this.rectangle = createBoundingRectangle(graphics);
        this.coordinates = new GridPoint2(initialCoordinates);
        this.destinationCoordinates = new GridPoint2(initialCoordinates);
    }

    // в занятую клетку не едем, только поворачиваемся
    void move(Direction direction, Obstacles obstacles) {
        if (!isEqual(movementProgress, 1f)) {
            return;
        }
        GridPoint2 destination = direction.nextCoordinates(coordinates);
        if (obstacles.isFree(destination)) {
            destinationCoordinates.set(destination);
            movementProgress = 0f;
        }
        rotation = direction.getRotation();
    }

    void update(float deltaTime, TileMovement tileMovement) {
        // calculate interpolated player screen coordinates
        tileMovement.moveRectangleBetweenTileCenters(rectangle, coordinates, destinationCoordinates, movementProgress);

        movementProgress = continueProgress(movementProgress, deltaTime, MOVEMENT_SPEED);
        if (isEqual(movementProgress, 1f)) {
            // record that the player has reached his/her destination
            coordinates.set(destinationCoordinates);
        }
    }

    void draw(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, rotation);
    }
}

class Tree {

    private final TextureRegion graphics;
    private final Rectangle rectangle;
    private final GridPoint2 coordinates;

    Tree(TextureRegion graphics, GridPoint2 coordinates, TiledMapTileLayer groundLayer) {
        this.graphics = graphics;
        this.coordinates = new GridPoint2(coordinates);
        this.rectangle = createBoundingRectangle(graphics);
        moveRectangleAtTileCenter(groundLayer, rectangle, this.coordinates);
    }

    boolean occupies(GridPoint2 otherCoordinates) {
        return coordinates.equals(otherCoordinates);
    }

    void draw(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, 0f);
    }
}

class GameField implements Obstacles {

    private final Tank player;
    private final List<Tree> trees = new ArrayList<>();

    GameField(Tank player) {
        this.player = player;
    }

    void addTree(Tree tree) {
        trees.add(tree);
    }

    void movePlayer(Direction direction) {
        player.move(direction, this);
    }

    @Override
    public boolean isFree(GridPoint2 coordinates) {
        for (Tree tree : trees) {
            if (tree.occupies(coordinates)) {
                return false;
            }
        }
        return true;
    }

    void update(float deltaTime, TileMovement tileMovement) {
        player.update(deltaTime, tileMovement);
    }

    void draw(Batch batch) {
        player.draw(batch);
        for (Tree tree : trees) {
            tree.draw(batch);
        }
    }
}

class PlayerInputController {

    private static final Map<Integer, Direction> KEY_TO_DIRECTION = new LinkedHashMap<>();

    static {
        KEY_TO_DIRECTION.put(UP, Direction.UP);
        KEY_TO_DIRECTION.put(W, Direction.UP);
        KEY_TO_DIRECTION.put(LEFT, Direction.LEFT);
        KEY_TO_DIRECTION.put(A, Direction.LEFT);
        KEY_TO_DIRECTION.put(DOWN, Direction.DOWN);
        KEY_TO_DIRECTION.put(S, Direction.DOWN);
        KEY_TO_DIRECTION.put(RIGHT, Direction.RIGHT);
        KEY_TO_DIRECTION.put(D, Direction.RIGHT);
    }

    Direction getPressedDirection() {
        for (Map.Entry<Integer, Direction> keyDirection : KEY_TO_DIRECTION.entrySet()) {
            if (Gdx.input.isKeyPressed(keyDirection.getKey())) {
                return keyDirection.getValue();
            }
        }
        return null;
    }
}
