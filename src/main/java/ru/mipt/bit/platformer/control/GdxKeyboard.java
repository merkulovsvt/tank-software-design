package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.Gdx;

public class GdxKeyboard implements Keyboard {

    @Override
    public boolean isPressed(int keyCode) {
        return Gdx.input.isKeyPressed(keyCode);
    }
}
