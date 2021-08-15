package io.github.teamcheeze.remoteActions.network.data;

import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.Packet;
import io.github.teamcheeze.remoteActions.network.PacketData;
import io.github.teamcheeze.remoteActions.server.Server;
import org.jetbrains.annotations.NotNull;

public class HelloPacket implements Packet<HelloPacket>, PacketData {
    private Server server;
    private Client client;
    private Connection connection;

    public HelloPacket() {

    }

    public HelloPacket(Client client) {
        this.client = client;
    }
    @Override
    public @NotNull HelloPacket getData() {
        return this;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public Server getServer() {
        return this.server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public void updateData(Packet<HelloPacket> updated) {
        this.client = updated.getData().getClient();
        this.server = updated.getData().getServer();
        this.connection = updated.getData().getConnection();
    }
}
