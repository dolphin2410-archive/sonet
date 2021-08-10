package io.github.teamcheeze.remoteActions.network.data.packets;

import io.github.teamcheeze.remoteActions.network.Packet;
import io.github.teamcheeze.remoteActions.network.data.fs.FileReadRequest;
import org.jetbrains.annotations.NotNull;

public class ServerFileReadPacket implements Packet<FileReadRequest> {
    private Object result;
    private FileReadRequest data;
    public ServerFileReadPacket() {

    }
    public ServerFileReadPacket(FileReadRequest data) {
        this.data = data;
    }
    @Override
    public Object getResult() {
        return result;
    }

    @Override
    public void updateData(Packet<FileReadRequest> updated) {
        this.result = updated.getResult();
        this.data = updated.getData();
    }

    @NotNull
    @Override
    public FileReadRequest getData() {
        return data;
    }

    private void setResult(Object result) {
        this.result = result;
    }
}
