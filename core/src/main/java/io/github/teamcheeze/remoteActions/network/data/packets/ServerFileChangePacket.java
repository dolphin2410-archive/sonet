package io.github.teamcheeze.remoteActions.network.data.packets;

import io.github.teamcheeze.remoteActions.network.Packet;
import io.github.teamcheeze.remoteActions.network.data.fs.FileChangeRequest;
import org.jetbrains.annotations.NotNull;

public class ServerFileChangePacket implements Packet<FileChangeRequest> {
    private FileChangeRequest data;
    private Object result;
    public ServerFileChangePacket() {

    }
    public ServerFileChangePacket(FileChangeRequest data) {
        this.data = data;
    }

    @Override
    public Object getResult() {
        return result;
    }

    private void setResult(Object result) {
        this.result = result;
    }

    @Override
    public @NotNull FileChangeRequest getData() {
        return data;
    }
}
