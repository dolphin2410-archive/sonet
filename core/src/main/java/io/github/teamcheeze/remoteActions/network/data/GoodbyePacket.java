package io.github.teamcheeze.remoteActions.network.data;

import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.Packet;
import io.github.teamcheeze.remoteActions.network.connection.IConnectionHandler;
import io.github.teamcheeze.remoteActions.server.IServer;
import org.jetbrains.annotations.NotNull;

public class GoodbyePacket implements Packet<GoodbyePacket> {
    private Connection connection;
    private boolean success = false;
    
    public GoodbyePacket(Connection connection) {
        this.connection = connection;
    }

    public void goodbye() {
        IConnectionHandler.removeClient(getConnection().getClient().getId());
        boolean result = ((IServer) connection.getServer()).getConnections().removeIf(con -> con.getClient() == getConnection().getClient());
        setSuccess(true);
        System.out.println("Remove from connections result = "+result);
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public @NotNull GoodbyePacket getData() {
        return this;
    }

    @Override
    public void updateData(Packet<GoodbyePacket> updated) {
        this.connection = updated.getData().getConnection();
        this.success = updated.getData().isSuccess();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
