package io.github.teamcheeze.remoteActions.server.features;

import io.github.teamcheeze.remoteActions.server.KeyboardKeys;

public interface ServerKeyboardSystem extends ServerSideSystem {
    default void pressKey(KeyboardKeys key) {
        pressKeys(0, key);
    }
    default void pressKeys(KeyboardKeys... keys) {
        pressKeys(0, keys);
    }
    default void pressKey(long duration, KeyboardKeys key) {
        pressKeys(duration, key);
    }
    void pressKeys(long duration, KeyboardKeys... keys);
}
