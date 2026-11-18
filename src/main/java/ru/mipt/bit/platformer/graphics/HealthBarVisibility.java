package ru.mipt.bit.platformer.graphics;

public class HealthBarVisibility {

    private boolean visible = false;

    public void toggle() {
        visible = !visible;
    }

    public boolean isVisible() {
        return visible;
    }
}
