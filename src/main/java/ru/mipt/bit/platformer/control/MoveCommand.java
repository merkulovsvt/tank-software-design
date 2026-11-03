package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;

public class MoveCommand implements GameCommand {

    private final GameField field;
    private final Direction direction;

    public MoveCommand(GameField field, Direction direction) {
        this.field = field;
        this.direction = direction;
    }

    @Override
    public void execute() {
        field.movePlayer(direction);
    }
}
