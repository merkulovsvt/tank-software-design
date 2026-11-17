package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.graphics.HealthBarVisibility;

/**
 * Application, use case "toggle health bars": implements the GameCommand port.
 * Missing port: depends on graphics.HealthBarVisibility directly, so the application knows about infrastructure.
 * An interface here implemented in graphics would fix it.
 */
public class ToggleHealthBarsCommand implements GameCommand {

    private final HealthBarVisibility visibility;

    public ToggleHealthBarsCommand(HealthBarVisibility visibility) {
        this.visibility = visibility;
    }

    @Override
    public void execute() {
        visibility.toggle();
    }
}
