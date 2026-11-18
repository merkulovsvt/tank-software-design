package ru.mipt.bit.platformer.control;

/**
 * Port to the input device: lets PlayerInputController work without libGDX.
 */
public interface Keyboard {

    boolean isPressed(int keyCode);

    boolean isJustPressed(int keyCode);
}
