package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.Gdx;

/**
 * Real keyboard backed by libGDX. Not unit-tested: it only forwards to the engine.
 */
public class GdxKeyboard implements Keyboard {

    @Override
    public boolean isPressed(int keyCode) {
        return Gdx.input.isKeyPressed(keyCode);
    }
}
