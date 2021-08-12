package io.github.teamcheeze.remoteActions.network.data.packets;

import io.github.teamcheeze.remoteActions.network.Packet;
import io.github.teamcheeze.remoteActions.network.data.server.HelloRequest;
import org.jetbrains.annotations.NotNull;

public class HelloPacket implements Packet<HelloRequest> {
    private HelloRequest data;

    public HelloPacket() {

    }
    public HelloPacket(HelloRequest request) {
        this.data = request;
    }

    @Override
    public @NotNull HelloRequest getData() {
        return data;
    }

    @Override
    public void updateData(Packet<HelloRequest> updated) {
        this.data = updated.getData();
    }
}
