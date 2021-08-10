package io.github.teamcheeze.remoteActions.network.data.fs;

import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.PacketData;
import io.github.teamcheeze.remoteActions.network.data.packets.ServerFileReadPacket;
import io.github.teamcheeze.remoteActions.server.Server;

import java.io.File;

public class FileReadRequest implements PacketData {
    private File result;
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

    public String getRequestedFilename() {
        return requestedFilename;
    }

    public File getResult() {
        return result;
    }

    public void setResult(File result) {
        this.result = result;
    }

}
