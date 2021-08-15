package io.github.teamcheeze.ctrl.packets.fs;

import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.PacketData;
import io.github.teamcheeze.remoteActions.server.Server;
import io.github.teamcheeze.remoteActions.util.RemoteFile;

public class FileReadRequest implements PacketData {
    private RemoteFile file;
    private String requestedFilename;
    private Server server;
    private Client client;
    public FileReadRequest() {

    }
    public FileReadRequest(Connection connection, String requestedFilename) {
        this.requestedFilename = requestedFilename;
        this.server = connection.getServer();
        this.client = connection.getClient();
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
