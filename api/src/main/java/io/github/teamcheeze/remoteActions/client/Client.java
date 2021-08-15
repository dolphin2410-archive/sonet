package io.github.teamcheeze.remoteActions.client;

import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.NetworkComponent;
import io.github.teamcheeze.remoteActions.network.Packet;
import io.github.teamcheeze.remoteActions.network.PacketData;
import io.github.teamcheeze.remoteActions.network.client.ClientAddress;
import io.github.teamcheeze.remoteActions.network.server.ServerAddress;
import io.github.teamcheeze.remoteActions.util.Machine;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

public interface Client extends NetworkComponent {

    /**
     * Connect to a server.
     * @param ipv4 The ip of the server
     * @param port The port of the server
     * @return Connection
     * @throws IOException When invalid host
     */
    @NotNull
    Connection connect(InetAddress ipv4, int port) throws IOException;

    /**
     * Connects to a server with the address instance.
     * @param address The address
     * @return The established connection.
     * @throws IOException when there was an I/O Exception during the connection.
     */
    @NotNull
    default Connection connect(ServerAddress address) throws IOException {
        return connect(address.ip, address.port);
    }

    @NotNull
    default Connection connect(int port) throws IOException {
        return connect(Machine.localIpAddress, port);
    }

    <R extends PacketData, T extends Packet<R>> T sendPacket(T packet);

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
    @NotNull ClientAddress getAddress();

    Connection getConnection();

    void abort();
}
