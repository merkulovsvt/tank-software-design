package ru.mipt.bit.platformer.graphics;

/**
 * Infrastructure (view state): whether health bars are shown.
 * Would be the adapter of the missing port used by ToggleHealthBarsCommand.
 */
public class HealthBarVisibility {

    private boolean visible = false;

    public void toggle() {
        visible = !visible;
    }

    public boolean isVisible() {
        return visible;
    }
}
