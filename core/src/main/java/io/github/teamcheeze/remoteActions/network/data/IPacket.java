package io.github.teamcheeze.remoteActions.network.data;

import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.Packet;
import org.jetbrains.annotations.NotNull;

public class IPacket<T> implements Packet<T> {
    private final T data;
    public IPacket(T data) {
        this.data = data;
    }
    @NotNull
    @Override
    public T getData() {
        return data;
    }

    @Override
    public void send(Connection connection) {
        connection.getClient().sendPacket(this);
    }
}
