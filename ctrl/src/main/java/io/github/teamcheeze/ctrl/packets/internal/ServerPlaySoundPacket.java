package io.github.teamcheeze.ctrl.packets.internal;

import io.github.teamcheeze.remoteActions.network.Packet;
import io.github.teamcheeze.remoteActions.network.PacketData;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;

public class ServerPlaySoundPacket implements PacketData, Packet<ServerPlaySoundPacket> {
    private byte[] inputStream;
    public ServerPlaySoundPacket() {

    }

    public ServerPlaySoundPacket(FileInputStream inputStream) {
        try {
            this.inputStream = inputStream.readAllBytes();
        } catch (IOException ignored) {

        }
    }

    public ServerPlaySoundPacket(File file) {
        try {
            this.inputStream = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setInputStream(FileInputStream inputStream) {
        try {
            this.inputStream = inputStream.readAllBytes();
        } catch (IOException ignored) {

        }
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(inputStream);
    }

    @Override
    public @NotNull ServerPlaySoundPacket getData() {
        return this;
    }



    @Override
    public void updateData(Packet<ServerPlaySoundPacket> updated) {
        this.inputStream = updated.getData().inputStream;
    }
}
