package ru.mipt.bit.platformer.model.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HealthStateTest {

    @Test
    void moreThanSeventyPercentIsLight() {
        assertInstanceOf(LightState.class, HealthState.of(1f));
        assertInstanceOf(LightState.class, HealthState.of(0.71f));
    }

    @Test
    void fromFifteenToSeventyPercentIsMedium() {
        assertInstanceOf(MediumState.class, HealthState.of(0.7f));
        assertInstanceOf(MediumState.class, HealthState.of(0.4f));
        assertInstanceOf(MediumState.class, HealthState.of(0.15f));
    }

    @Test
    void lessThanFifteenPercentIsHeavy() {
        assertInstanceOf(HeavyState.class, HealthState.of(0.14f));
        assertInstanceOf(HeavyState.class, HealthState.of(0f));
    }

    @Test
    void sharesComputedFromHealthHitTheBoundaries() {
        assertInstanceOf(MediumState.class, HealthState.of(70 / 100f));
        assertInstanceOf(MediumState.class, HealthState.of(15 / 100f));
    }

    @Test
    void lightStateKeepsTankAsUndamaged() {
        TankState state = new LightState();
        assertEquals(0.4f, state.movementTime(0.4f));
        assertEquals(0.5f, state.reloadTime(0.5f));
        assertTrue(state.canShoot());
    }

    @Test
    void mediumStateMovesTwiceSlower() {
        TankState state = new MediumState();
        assertEquals(0.8f, state.movementTime(0.4f));
        assertEquals(0.5f, state.reloadTime(0.5f));
        assertTrue(state.canShoot());
    }

    @Test
    void heavyStateMovesThreeTimesSlowerAndCannotShoot() {
        TankState state = new HeavyState();
        assertEquals(1.2f, state.movementTime(0.4f), 0.0001f);
        assertFalse(state.canShoot());
    }

    @Test
    void stateFollowsHealthBothWays() {
        TankState state = new LightState();
        state = state.onHealthChanged(0.5f);
        assertInstanceOf(MediumState.class, state);
        state = state.onHealthChanged(0.1f);
        assertInstanceOf(HeavyState.class, state);
        state = state.onHealthChanged(0.9f);
        assertInstanceOf(LightState.class, state);
    }
}
