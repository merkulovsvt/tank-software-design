package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.control.GdxKeyboard;
import ru.mipt.bit.platformer.control.MoveCommand;
import ru.mipt.bit.platformer.control.PlayerInputController;
import ru.mipt.bit.platformer.graphics.GameRenderer;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;

public class GameDesktopLauncher implements ApplicationListener {

    private static final String LEVEL_FILE = "level.tmx";
    private static final GridPoint2 TANK_START = new GridPoint2(1, 1);
    private static final GridPoint2 TREE_POSITION = new GridPoint2(1, 3);
    private static final String TANK_TEXTURE = "images/tank_blue.png";
    private static final String TREE_TEXTURE = "images/greenTree.png";
    private static final float TANK_SPEED = 0.4f;

    private GameField field;
    private GameRenderer renderer;
    private PlayerInputController inputController;

    @Override
    public void create() {
        Tank tank = new Tank(TANK_START, TANK_SPEED);
        Tree tree = new Tree(TREE_POSITION);

        field = new GameField(tank);
        field.addObstacle(tree);

        renderer = new GameRenderer(LEVEL_FILE);
        renderer.addObject(tank, TANK_TEXTURE);
        renderer.addObject(tree, TREE_TEXTURE);

        inputController = new PlayerInputController(new GdxKeyboard())
                .bindKey(UP, new MoveCommand(field, Direction.UP))
                .bindKey(W, new MoveCommand(field, Direction.UP))
                .bindKey(LEFT, new MoveCommand(field, Direction.LEFT))
                .bindKey(A, new MoveCommand(field, Direction.LEFT))
                .bindKey(DOWN, new MoveCommand(field, Direction.DOWN))
                .bindKey(S, new MoveCommand(field, Direction.DOWN))
                .bindKey(RIGHT, new MoveCommand(field, Direction.RIGHT))
                .bindKey(D, new MoveCommand(field, Direction.RIGHT));
    }

    @Override
    public void render() {
        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();

        inputController.handleInput();
        field.update(deltaTime);
        renderer.render();
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
        renderer.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
