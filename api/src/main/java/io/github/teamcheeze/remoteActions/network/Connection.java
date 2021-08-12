package io.github.teamcheeze.remoteActions.network;

import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.server.Server;
import io.github.teamcheeze.remoteActions.server.features.*;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.net.ServerSocket;

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
     * The server socket.
     * @return The connection's server socket.
     */
    @NotNull ServerSocket getServerSocket();

    /**
     * Aborts the connection.
     */
    void abort();

    /**
     * Send a packet to the server.
     * @param packet Packet
     * @param <R> PacketData type
     * @param <T> PacketType
     * @return The returned packet. The server returns a different instance, but automatically syncs to the sent packet.
     */
    <R extends PacketData, T extends Packet<R>> T sendPacket(T packet);

    ServerFileSystem fs();

    ServerKeyboardSystem keyboard();

    ServerMouseSystem mouse();

    ServerSoundSystem sound();

    ServerTasksSystem tasks();

    boolean isValid();

    void validate();
}
