package io.github.teamcheeze.remoteActions.network.data.packets;

import io.github.teamcheeze.remoteActions.network.Packet;
import io.github.teamcheeze.remoteActions.network.PacketData;
import io.github.teamcheeze.remoteActions.server.KeyboardKeys;
import org.jetbrains.annotations.NotNull;

public class KeyPressPacket implements PacketData, Packet<KeyPressPacket> {
    private long duration;
    private KeyboardKeys[] keys;
    public KeyPressPacket() {

    }
    public KeyPressPacket(long duration, KeyboardKeys... keys) {
        this.duration = duration;
        this.keys = keys;
    }

    @Override
    public @NotNull KeyPressPacket getData() {
        return this;
    }

    @Override
    public void updateData(Packet<KeyPressPacket> updated) {
        this.duration = updated.getData().getDuration();
        this.keys = updated.getData().getKeys();
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setKeys(KeyboardKeys[] keys) {
        this.keys = keys;
    }

    public KeyboardKeys[] getKeys() {
        return keys;
    }

    public long getDuration() {
        return duration;
    }
}
