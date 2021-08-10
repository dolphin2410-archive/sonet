package io.github.teamcheeze.remoteActions;

import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.client.IClient;
import io.github.teamcheeze.remoteActions.network.connection.IConnectionHandler;
import io.github.teamcheeze.remoteActions.server.IServer;
import io.github.teamcheeze.remoteActions.server.Server;

public class IRemoteActionsSupplier implements RemoteActionsSupplier {
    @Override
    public Server hostServer(int port) {
        IServer server = new IServer(port);
        server.initialize();
        IConnectionHandler.registerServer(server);
        return server;
    }

    @Override
    public Client registerClient() {
        IClient client = new IClient();
        client.initialize();
        IConnectionHandler.registerClient(client);
        return client;
    }
}
