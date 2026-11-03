package ru.mipt.bit.platformer.control;

/**
 * Source of key presses. Extracting it keeps {@link PlayerInputController} free of libGDX,
 * so the key-to-command mapping can be unit-tested with a fake keyboard.
 */
public interface Keyboard {

    boolean isPressed(int keyCode);
}
