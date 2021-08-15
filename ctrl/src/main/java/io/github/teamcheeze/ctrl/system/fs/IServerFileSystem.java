package io.github.teamcheeze.ctrl.system.fs;

import io.github.teamcheeze.ctrl.packets.internal.ServerFileReadPacket;
import io.github.teamcheeze.ctrl.system.features.ServerFileSystem;
import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.util.RemoteFile;

public class IServerFileSystem implements ServerFileSystem {
    private Connection connection;
    public IServerFileSystem(Connection connection) {
        this.connection = connection;
    }
    @Override
    public RemoteFile getFile(String filename) {
        connection.validate();
        ServerFileReadPacket packet = new ServerFileReadPacket(connection, filename);
        connection.sendPacket(packet);
        return packet.getFile();
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}
