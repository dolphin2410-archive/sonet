package io.github.teamcheeze.ctrl.packets.internal;

import io.github.teamcheeze.ctrl.packets.fs.FileChangeRequest;
import io.github.teamcheeze.remoteActions.network.Packet;
import org.jetbrains.annotations.NotNull;

public class ServerFileChangePacket implements Packet<FileChangeRequest> {
    private FileChangeRequest data;
    public ServerFileChangePacket() {

    }
    public ServerFileChangePacket(FileChangeRequest data) {
        this.data = data;
    }

    @Override
    public void updateData(Packet<FileChangeRequest> updated) {
        this.data = updated.getData();
    }

    @Override
    public @NotNull FileChangeRequest getData() {
        return data;
    }
}
