package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileLevelLoader implements LevelLoader {

    private static final char TREE = 'T';
    private static final char PLAYER = 'X';
    private static final char EMPTY = '_';

    private final String resourceName;

    public FileLevelLoader(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public LevelLayout load() {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(resourceName);
        if (stream == null) {
            throw new IllegalArgumentException("Level file not found: " + resourceName);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            return parse(reader.lines().collect(Collectors.toList()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static LevelLayout parse(List<String> lines) {
        List<String> rows = new ArrayList<>();
        for (String line : lines) {
            if (!line.isBlank()) {
                rows.add(line.strip());
            }
        }

        GridPoint2 playerStart = null;
        List<GridPoint2> trees = new ArrayList<>();
        // first line of the file is the top row of the level, y grows upwards
        for (int row = 0; row < rows.size(); row++) {
            int y = rows.size() - 1 - row;
            String line = rows.get(row);
            for (int x = 0; x < line.length(); x++) {
                char cell = line.charAt(x);
                if (cell == TREE) {
                    trees.add(new GridPoint2(x, y));
                } else if (cell == PLAYER) {
                    if (playerStart != null) {
                        throw new IllegalArgumentException("Level has more than one player start");
                    }
                    playerStart = new GridPoint2(x, y);
                } else if (cell != EMPTY) {
                    throw new IllegalArgumentException("Unknown cell '" + cell + "' at line " + (row + 1));
                }
            }
        }
        if (playerStart == null) {
            throw new IllegalArgumentException("Level has no player start");
        }
        return new LevelLayout(playerStart, trees);
    }
}
