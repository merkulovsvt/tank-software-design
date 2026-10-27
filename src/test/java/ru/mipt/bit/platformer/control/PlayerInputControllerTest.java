package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerInputControllerTest {

    /** Fake keyboard: only the keys we mark as pressed report as pressed. */
    private static class FakeKeyboard implements Keyboard {
        private final Set<Integer> pressed = new HashSet<>();

        FakeKeyboard press(int... keys) {
            for (int key : keys) {
                pressed.add(key);
            }
            return this;
        }

        @Override
        public boolean isPressed(int keyCode) {
            return pressed.contains(keyCode);
        }
    }

    /** Records that it ran, in call order. */
    private static class RecordingCommand implements GameCommand {
        private final String name;
        private final List<String> log;

        RecordingCommand(String name, List<String> log) {
            this.name = name;
            this.log = log;
        }

        @Override
        public void execute() {
            log.add(name);
        }
    }

    @Test
    void runsOnlyTheCommandsWhoseKeyIsPressed() {
        List<String> log = new ArrayList<>();
        PlayerInputController controller = new PlayerInputController(new FakeKeyboard().press(20))
                .bindKey(10, new RecordingCommand("a", log))
                .bindKey(20, new RecordingCommand("b", log))
                .bindKey(30, new RecordingCommand("c", log));

        controller.handleInput();

        assertEquals(List.of("b"), log);
    }

    @Test
    void nothingRunsWhenNoBoundKeyIsPressed() {
        List<String> log = new ArrayList<>();
        PlayerInputController controller = new PlayerInputController(new FakeKeyboard())
                .bindKey(10, new RecordingCommand("a", log));

        controller.handleInput();

        assertEquals(List.of(), log);
    }

    @Test
    void severalPressedCommandsRunInBindingOrder() {
        List<String> log = new ArrayList<>();
        PlayerInputController controller = new PlayerInputController(new FakeKeyboard().press(10, 20))
                .bindKey(10, new RecordingCommand("first", log))
                .bindKey(20, new RecordingCommand("second", log));

        controller.handleInput();

        assertEquals(List.of("first", "second"), log);
    }
}
