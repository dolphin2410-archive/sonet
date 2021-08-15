package io.github.teamcheeze.ctrl.packets.internal;

import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.Packet;
import io.github.teamcheeze.remoteActions.network.PacketData;
import io.github.teamcheeze.remoteActions.server.Server;
import io.github.teamcheeze.remoteActions.util.RemoteFile;
import org.jetbrains.annotations.NotNull;

public class ServerFileReadPacket implements PacketData, Packet<ServerFileReadPacket> {
    private RemoteFile file;
    private String requestedFilename;
    private Server server;
    private Client client;
    public ServerFileReadPacket() {

    }
    public ServerFileReadPacket(Connection connection, String requestedFilename) {
        connection.validate();
        this.requestedFilename = requestedFilename;
    }
    @Override
    public void updateData(Packet<ServerFileReadPacket> updated) {
        this.file = updated.getData().getFile();
        this.server = updated.getData().getServer();
        this.client = updated.getData().getClient();
        this.requestedFilename = updated.getData().getRequestedFilename();
    }

    @NotNull
    @Override
    public ServerFileReadPacket getData() {
        return this;
    }

    public Server getServer() {
        return server;
    }

    public Client getClient() {
        return client;
    }

    public void setRequestedFilename(String requestedFilename) { this.requestedFilename = requestedFilename; }

    public String getRequestedFilename() {
        return requestedFilename;
    }

    public RemoteFile getFile() {
        return file;
    }

    public void setFile(RemoteFile file) {
        this.file = file;
    }
}
