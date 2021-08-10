package io.github.teamcheeze.remoteActions.network.data.packets;

import io.github.teamcheeze.remoteActions.network.Packet;
import io.github.teamcheeze.remoteActions.network.data.server.ServerIdentityRequest;
import org.jetbrains.annotations.NotNull;

public class ServerIdentityPacket implements Packet<ServerIdentityRequest> {

    private Object result;
    private ServerIdentityRequest data;

    public ServerIdentityPacket() {

    }
    public ServerIdentityPacket(ServerIdentityRequest request) {
        this.data = request;
    }

    @Override
    public @NotNull ServerIdentityRequest getData() {
        return data;
    }

    @Override
    public Object getResult() {
        return result;
    }

    @Override
    public void updateData(Packet<ServerIdentityRequest> updated) {
        this.result = updated.getResult();
        this.data = updated.getData();
    }

    private void setResult(Object result) {
        this.result = result;
    }
}
