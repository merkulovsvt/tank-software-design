package ru.mipt.bit.platformer.control;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlayerInputController implements TankController {

    private final Keyboard keyboard;
    private final Map<Integer, GameCommand> bindings = new LinkedHashMap<>();

    public PlayerInputController(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    public PlayerInputController bindKey(int keyCode, GameCommand command) {
        bindings.put(keyCode, command);
        return this;
    }

    @Override
    public List<GameCommand> nextCommands() {
        List<GameCommand> commands = new ArrayList<>();
        for (Map.Entry<Integer, GameCommand> binding : bindings.entrySet()) {
            if (keyboard.isPressed(binding.getKey())) {
                commands.add(binding.getValue());
            }
        }
        return commands;
    }
}
