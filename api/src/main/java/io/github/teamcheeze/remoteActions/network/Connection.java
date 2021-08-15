package io.github.teamcheeze.remoteActions.network;

import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.server.Server;
import org.jetbrains.annotations.NotNull;
import java.io.Serializable;

public interface Connection extends Serializable {
    /**
     * Gets the client of the connection.
     * @return The client side.
     */
    @NotNull Client getClient();

    /**
     * Gets the server of the connection.
     * @return The server side.
     */
    @NotNull Server getServer();
    /**
     * Aborts the connection.
     */
    void abort();

    <R extends PacketData, T extends Packet<R>> T sendPacket(T packet);

//    /**
//     * Send a packet to the server.
//     * @param packet Packet
//     * @param <R> PacketData type
//     * @param <T> PacketType
//     * @return The returned packet. The server returns a different instance, but automatically syncs to the sent packet.
//     */
//    <R extends PacketData, T extends Packet<R>> T sendPacket(T packet);

    boolean isValid();

    void validate();
}
