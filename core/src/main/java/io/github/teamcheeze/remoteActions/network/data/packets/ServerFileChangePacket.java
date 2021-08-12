package io.github.teamcheeze.remoteActions.network.data.packets;

import io.github.teamcheeze.remoteActions.network.Packet;
import io.github.teamcheeze.remoteActions.network.data.fs.FileChangeRequest;
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
