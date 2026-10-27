package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.control.GameCommand;
import ru.mipt.bit.platformer.control.GdxKeyboard;
import ru.mipt.bit.platformer.control.MoveCommand;
import ru.mipt.bit.platformer.control.PlayerInputController;
import ru.mipt.bit.platformer.control.RandomTankController;
import ru.mipt.bit.platformer.control.TankController;
import ru.mipt.bit.platformer.graphics.GameRenderer;
import ru.mipt.bit.platformer.level.FileLevelLoader;
import ru.mipt.bit.platformer.level.LevelLayout;
import ru.mipt.bit.platformer.level.LevelLoader;
import ru.mipt.bit.platformer.level.RandomLevelGenerator;
import ru.mipt.bit.platformer.level.RandomTankGenerator;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private static final String DEFAULT_LEVEL = "levels/level1.txt";
    private static final int LEVEL_WIDTH = 10;
    private static final int LEVEL_HEIGHT = 8;
    private static final int RANDOM_TREE_COUNT = 15;
    private static final int ENEMY_TANK_COUNT = 4;
    private static final String TANK_TEXTURE = "images/tank_blue.png";
    private static final String TREE_TEXTURE = "images/greenTree.png";
    private static final float TANK_SPEED = 0.4f;

    private GameField field;
    private GameRenderer renderer;
    private final List<TankController> controllers = new ArrayList<>();

    private final LevelLoader levelLoader;

    public GameDesktopLauncher(LevelLoader levelLoader) {
        this.levelLoader = levelLoader;
    }

    @Override
    public void create() {
        LevelLayout layout = levelLoader.load();
        Random random = new Random();

        field = new GameField(LEVEL_WIDTH, LEVEL_HEIGHT);
        renderer = new GameRenderer(LEVEL_FILE);

        Tank player = new Tank(layout.getPlayerStart(), TANK_SPEED);
        field.addTank(player);
        renderer.addObject(player, TANK_TEXTURE);

        for (GridPoint2 treeCoordinates : layout.getTreeCoordinates()) {
            Tree tree = new Tree(treeCoordinates);
            field.addObstacle(tree);
            renderer.addObject(tree, TREE_TEXTURE);
        }

        controllers.add(new PlayerInputController(new GdxKeyboard())
                .bindKey(UP, new MoveCommand(player, field, Direction.UP))
                .bindKey(W, new MoveCommand(player, field, Direction.UP))
                .bindKey(LEFT, new MoveCommand(player, field, Direction.LEFT))
                .bindKey(A, new MoveCommand(player, field, Direction.LEFT))
                .bindKey(DOWN, new MoveCommand(player, field, Direction.DOWN))
                .bindKey(S, new MoveCommand(player, field, Direction.DOWN))
                .bindKey(RIGHT, new MoveCommand(player, field, Direction.RIGHT))
                .bindKey(D, new MoveCommand(player, field, Direction.RIGHT)));

        RandomTankGenerator tankGenerator = new RandomTankGenerator(LEVEL_WIDTH, LEVEL_HEIGHT, ENEMY_TANK_COUNT, random);
        for (GridPoint2 enemyStart : tankGenerator.generate(layout)) {
            Tank enemy = new Tank(enemyStart, TANK_SPEED);
            field.addTank(enemy);
            renderer.addObject(enemy, TANK_TEXTURE);
            controllers.add(new RandomTankController(enemy, field, random));
        }
    }

    @Override
    public void render() {
        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();

        for (TankController controller : controllers) {
            for (GameCommand command : controller.nextCommands()) {
                command.execute();
            }
        }
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
        new Lwjgl3Application(new GameDesktopLauncher(createLevelLoader(args)), config);
    }

    // ./gradlew run --args="random" - random level, ./gradlew run --args="levels/other.txt" - level from file
    private static LevelLoader createLevelLoader(String[] args) {
        if (args.length > 0 && args[0].equals("random")) {
            return new RandomLevelGenerator(LEVEL_WIDTH, LEVEL_HEIGHT, RANDOM_TREE_COUNT, new Random());
        }
        String levelFile = args.length > 0 ? args[0] : DEFAULT_LEVEL;
        return new FileLevelLoader(levelFile);
    }
}
