package io.github.teamcheeze.remoteActions.network;

import org.jetbrains.annotations.NotNull;

/**
 * Packet that will be transferred through the socket
 * @param <T> Data Type
 */
public interface Packet<T> {
    @NotNull T getData();
    Object getResult();
    default void send(Connection connection) {
        connection.getClient().sendPacket(this);
    }
}
