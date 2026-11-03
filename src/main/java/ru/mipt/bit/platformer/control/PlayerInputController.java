package ru.mipt.bit.platformer.control;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Binds keys to {@link GameCommand}s and runs the ones whose key is pressed.
 * <p>
 * A new handler (for example {@code SPACE -> fire}) is a single {@link #bindKey} call plus a
 * {@link GameCommand} class — no new {@code if} branch and no change to the game loop.
 * Keys are checked in binding order, which is their priority when several are held at once.
 */
public class PlayerInputController {

    private final Keyboard keyboard;
    private final Map<Integer, GameCommand> bindings = new LinkedHashMap<>();

    public PlayerInputController(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    /** Bind a key code to a command. Returns {@code this} so bindings can be chained. */
    public PlayerInputController bindKey(int keyCode, GameCommand command) {
        bindings.put(keyCode, command);
        return this;
    }

    /** Execute every bound command whose key is currently pressed, in binding order. */
    public void handleInput() {
        for (Map.Entry<Integer, GameCommand> binding : bindings.entrySet()) {
            if (keyboard.isPressed(binding.getKey())) {
                binding.getValue().execute();
            }
        }
    }
}
