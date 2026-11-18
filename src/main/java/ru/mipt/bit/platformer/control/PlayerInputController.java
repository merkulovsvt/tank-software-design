package ru.mipt.bit.platformer.control;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntPredicate;

public class PlayerInputController implements TankController {

    private final Keyboard keyboard;
    private final Map<Integer, GameCommand> bindings = new LinkedHashMap<>();
    private final Map<Integer, GameCommand> pressBindings = new LinkedHashMap<>();

    public PlayerInputController(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    public PlayerInputController bindKey(int keyCode, GameCommand command) {
        bindings.put(keyCode, command);
        return this;
    }

    public PlayerInputController bindKeyPress(int keyCode, GameCommand command) {
        pressBindings.put(keyCode, command);
        return this;
    }

    @Override
    public List<GameCommand> nextCommands() {
        List<GameCommand> commands = new ArrayList<>();
        collectCommands(bindings, keyboard::isPressed, commands);
        collectCommands(pressBindings, keyboard::isJustPressed, commands);
        return commands;
    }

    private static void collectCommands(Map<Integer, GameCommand> bindings, IntPredicate isActive, List<GameCommand> commands) {
        for (Map.Entry<Integer, GameCommand> binding : bindings.entrySet()) {
            if (isActive.test(binding.getKey())) {
                commands.add(binding.getValue());
            }
        }
    }
}
