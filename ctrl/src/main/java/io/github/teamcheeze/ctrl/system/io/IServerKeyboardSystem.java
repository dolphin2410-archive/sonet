package io.github.teamcheeze.ctrl.system.io;

import io.github.teamcheeze.ctrl.packets.internal.KeyPressPacket;
import io.github.teamcheeze.ctrl.system.features.ServerKeyboardSystem;
import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.server.KeyboardKeys;

public class IServerKeyboardSystem implements ServerKeyboardSystem {
    private Connection connection;
    public IServerKeyboardSystem(Connection connection) {
        this.connection = connection;
    }
    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void pressKeys(long duration, KeyboardKeys... keys) {
        connection.validate();
        KeyPressPacket packet = new KeyPressPacket(duration, keys);
        connection.sendPacket(packet);
    }
}
