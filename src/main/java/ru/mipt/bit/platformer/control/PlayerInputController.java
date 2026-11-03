package ru.mipt.bit.platformer.control;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerInputController {

    private final Keyboard keyboard;
    private final Map<Integer, GameCommand> bindings = new LinkedHashMap<>();

    public PlayerInputController(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    public PlayerInputController bindKey(int keyCode, GameCommand command) {
        bindings.put(keyCode, command);
        return this;
    }

    public void handleInput() {
        for (Map.Entry<Integer, GameCommand> binding : bindings.entrySet()) {
            if (keyboard.isPressed(binding.getKey())) {
                binding.getValue().execute();
            }
        }
    }
}
