package ru.mipt.bit.platformer.graphics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HealthBarVisibilityTest {

    @Test
    void hiddenByDefault() {
        assertFalse(new HealthBarVisibility().isVisible());
    }

    @Test
    void toggleSwitchesVisibilityBackAndForth() {
        HealthBarVisibility visibility = new HealthBarVisibility();

        visibility.toggle();
        assertTrue(visibility.isVisible());

        visibility.toggle();
        assertFalse(visibility.isVisible());
    }
}
