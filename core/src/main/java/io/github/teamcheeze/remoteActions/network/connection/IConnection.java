package io.github.teamcheeze.remoteActions.network.connection;

import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.Packet;
import io.github.teamcheeze.remoteActions.network.PacketData;
import io.github.teamcheeze.remoteActions.network.data.GoodbyePacket;
import io.github.teamcheeze.remoteActions.server.Server;
import io.github.teamcheeze.remoteActions.util.Listener;
import io.github.teamcheeze.remoteActions.util.Machine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.Socket;
import java.util.UUID;

/**
 * Connection should always be accepted by the server and be sent by the server.
 */
public class IConnection implements Connection {
    private final Client client;
    private final Server server;
    private boolean valid;
    private transient Listener<Packet<?>> listener = null;
    public IConnection(@NotNull UUID clientId, @NotNull UUID hostId) {
        this(IConnectionHandler.getClient(clientId), IConnectionHandler.getServer(hostId));
    }

    public IConnection(@Nullable Client client, @Nullable Server server) {
        if (client == null || server == null) {
            throw new RuntimeException("Neither server nor client can be null.");
        }
        if (!server.getAddress().getIp().getHostAddress().equals(Machine.localIpAddress.getHostAddress()))
            throw new RuntimeException("The connection instance can only be created on the server");
        this.valid = true;
        this.client = client;
        this.server = server;
    }

    @Override
    public void abort() {
        validate();
        GoodbyePacket packet = new GoodbyePacket(this);
        sendPacket(packet);
        setValid(false);

        if (!packet.getData().isSuccess()) {
            throw new RuntimeException("Error occurred while invalidating the connection");
        }
        IConnectionHandler.removeServer(this.getServer().getId());
    }

    @Override
    public <R extends PacketData, T extends Packet<R>> T sendPacket(T packet) {
        if (listener == null) {
            throw new RuntimeException("The client didn't initialize the connection");
        }
        listener.onAction(packet);
        return packet;
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

    @Override
    public boolean isValid() {
        return this.valid;
    }

    private void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public void validate() {
        if (!isValid()) {
            throw new RuntimeException("Trying to use an invalidated connection.");
        }
    }

    public void setListener(Listener<Packet<?>> listener) {
        this.listener = listener;
    }

    public Listener<Packet<?>> getListener() {
        return listener;
    }
}
