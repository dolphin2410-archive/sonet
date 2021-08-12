package io.github.teamcheeze.remoteActions.network.data.packets;

import io.github.teamcheeze.remoteActions.network.Packet;
import io.github.teamcheeze.remoteActions.network.data.media.PlaySoundRequest;
import org.jetbrains.annotations.NotNull;

public class ServerPlaySoundPacket implements Packet<PlaySoundRequest> {
    private PlaySoundRequest data;
    public ServerPlaySoundPacket(@NotNull PlaySoundRequest data) {
        this.data = data;
    }
    public ServerPlaySoundPacket() {

    }

    @Override
    public @NotNull PlaySoundRequest getData() {
        return data;
    }

    @Override
    public void updateData(Packet<PlaySoundRequest> updated) {
        this.data = updated.getData();
    }
}
