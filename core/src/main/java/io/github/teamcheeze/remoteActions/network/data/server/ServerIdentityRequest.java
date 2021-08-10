package io.github.teamcheeze.remoteActions.network.data.server;

import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.network.PacketData;
import io.github.teamcheeze.remoteActions.server.Server;

public class ServerIdentityRequest implements PacketData {
    private Server server;
    private Client client;
    public ServerIdentityRequest() {

    }
    public ServerIdentityRequest(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Server getServer() {
        return this.server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
}
