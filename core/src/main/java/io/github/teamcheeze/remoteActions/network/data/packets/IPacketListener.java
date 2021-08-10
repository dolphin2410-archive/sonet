package io.github.teamcheeze.remoteActions.network.data.packets;

import io.github.teamcheeze.remoteActions.network.data.PacketListener;

import java.util.function.Consumer;

public class IPacketListener extends PacketListener {
    private final Consumer<Object> onReceived;
    public IPacketListener(Consumer<Object> onReceived) {
        this.onReceived = onReceived;
    }

    @Override
    public void onReceived(Object object) {
        onReceived.accept(object);
    }
}
