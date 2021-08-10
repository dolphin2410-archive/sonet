package io.github.teamcheeze.remoteActions.client;

import io.github.teamcheeze.remoteActions.network.Address;
import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.Packet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.UUID;

public interface Client {

    /**
     * Connect to a server.
     * @param ipv4 The ip of the server
     * @param port The port of the server
     * @return Connection
     * @throws IOException When invalid host
     */
    @NotNull
    Connection connect(Inet4Address ipv4, int port) throws IOException;

    @NotNull
    default Connection connect(Address address) throws IOException {
        return connect(address.ipv4, address.port);
    }

    void sendPacket(Packet<?> packet);

    /**
     * The unique client's id
     * @return uuid
     */
    @NotNull
    UUID getId();

    /**
     * The client's system ip address
     * @return The client's system ip address
     */
    @NotNull Address getAddress();
}
