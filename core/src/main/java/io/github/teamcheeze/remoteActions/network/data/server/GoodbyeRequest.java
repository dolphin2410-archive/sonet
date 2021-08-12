package io.github.teamcheeze.remoteActions.network.data.server;

import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.PacketData;
import io.github.teamcheeze.remoteActions.network.connection.IConnectionHandler;

public class GoodbyeRequest implements PacketData {
    private Connection connection;
    private boolean success = false;
    public GoodbyeRequest(Connection connection) {
        this.connection = connection;
    }

    /**
     * Only to be called on the server. Aborts the server-side connection. Calling this method will remove the client from the registeredClients list on the server
     */
    public void goodbye() {
        System.out.println(IConnectionHandler.removeClient(connection.getClient()));;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
