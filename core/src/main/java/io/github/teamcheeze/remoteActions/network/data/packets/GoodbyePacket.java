package io.github.teamcheeze.remoteActions.network.data.packets;

import io.github.teamcheeze.remoteActions.network.Packet;
import io.github.teamcheeze.remoteActions.network.data.server.GoodbyeRequest;
import org.jetbrains.annotations.NotNull;

public class GoodbyePacket implements Packet<GoodbyeRequest> {
    private GoodbyeRequest data;

    public GoodbyePacket() {

    }
    public GoodbyePacket(GoodbyeRequest data) {
        this.data = data;
    }

    @Override
    public @NotNull GoodbyeRequest getData() {
        return data;
    }

    @Override
    public void updateData(Packet<GoodbyeRequest> updated) {

    }
}
