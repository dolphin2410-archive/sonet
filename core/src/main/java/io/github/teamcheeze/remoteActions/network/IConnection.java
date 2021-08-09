package io.github.teamcheeze.remoteActions.network;

import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.server.Server;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class IConnection implements Connection {
    private Client client;
    private Server server;
    public IConnection() {

    }
    public IConnection(@NotNull UUID clientId, @NotNull UUID hostId) {
        this.client = IConnectionHandler.getClient(clientId);
        this.server = IConnectionHandler.getServer(hostId);
    }
    @Override
    @NotNull
    public Client getClient() {
        return client;
    }

    @NotNull
    @Override
    public Server getServer() {
        return server;
    }
}
