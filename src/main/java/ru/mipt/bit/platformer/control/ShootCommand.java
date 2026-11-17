package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Battlefield;
import ru.mipt.bit.platformer.model.Tank;

/**
 * Application, use case "shoot": implements the GameCommand port by calling the domain.
 */
public class ShootCommand implements GameCommand {

    private final Tank tank;
    private final Battlefield battlefield;

    public ShootCommand(Tank tank, Battlefield battlefield) {
        this.tank = tank;
        this.battlefield = battlefield;
    }

    @Override
    public void execute() {
        tank.shoot(battlefield);
    }
}
