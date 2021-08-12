package io.github.teamcheeze.remoteActions.server.io;

import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.data.packets.KeyPressPacket;
import io.github.teamcheeze.remoteActions.server.KeyboardKeys;
import io.github.teamcheeze.remoteActions.server.features.ServerKeyboardSystem;

public class IServerKeyboardSystem implements ServerKeyboardSystem {
    private Connection connection;
    public IServerKeyboardSystem(Connection connection) {
        connection.validate();
        this.connection = connection;
    }
    @Override
    public Connection getConnection() {
        connection.validate();
        return connection;
    }

    @Override
    public void pressKeys(long duration, KeyboardKeys... keys) {
        connection.validate();
        KeyPressPacket packet = new KeyPressPacket(duration, keys);
        connection.sendPacket(packet);
    }
}
