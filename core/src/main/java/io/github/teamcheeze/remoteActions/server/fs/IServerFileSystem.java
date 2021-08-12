package io.github.teamcheeze.remoteActions.server.fs;

import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.data.fs.FileReadRequest;
import io.github.teamcheeze.remoteActions.network.data.packets.ServerFileReadPacket;
import io.github.teamcheeze.remoteActions.server.features.ServerFileSystem;
import io.github.teamcheeze.remoteActions.util.Machine;
import io.github.teamcheeze.remoteActions.util.RemoteFile;

import java.io.File;

public class IServerFileSystem implements ServerFileSystem {
    private Connection connection;
    public IServerFileSystem(Connection connection) {
        this.connection = connection;
    }
    @Override
    public RemoteFile getFile(String filename) {
        ServerFileReadPacket packet = new ServerFileReadPacket(connection, filename);
        connection.sendPacket(packet);
        return packet.getFile();
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}
