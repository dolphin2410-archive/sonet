package io.github.teamcheeze.remoteActions.network;

import org.jetbrains.annotations.NotNull;

/**
 * Packet that will be transferred through the socket
 * @param <T> Data Type
 */
public interface Packet<T extends PacketData> extends PacketData {
    @NotNull T getData();
    void updateData(Packet<T> updated);
    default void send(Connection connection) {
        connection.getClient().sendPacket(this);
    }
}
