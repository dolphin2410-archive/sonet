package io.github.teamcheeze.remoteActions.network;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Packet that will be transferred through the socket
 * @param <T> Data Type
 */
public interface Packet<T extends PacketData> extends Serializable {
    @NotNull T getData();
    Object getResult();
    void updateData(Packet<T> updated);
    default void send(Connection connection) {
        connection.getClient().sendPacket(connection, this);
    }
}
