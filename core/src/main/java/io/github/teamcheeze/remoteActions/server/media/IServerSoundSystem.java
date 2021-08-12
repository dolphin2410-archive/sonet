package io.github.teamcheeze.remoteActions.server.media;

import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.data.PacketListener;
import io.github.teamcheeze.remoteActions.network.data.media.PlaySoundRequest;
import io.github.teamcheeze.remoteActions.network.data.packets.IPacketListener;
import io.github.teamcheeze.remoteActions.network.data.packets.ServerPlaySoundPacket;
import io.github.teamcheeze.remoteActions.server.features.ServerSideSystem;
import io.github.teamcheeze.remoteActions.server.features.ServerSoundSystem;
import io.github.teamcheeze.remoteActions.util.Machine;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class IServerSoundSystem implements ServerSoundSystem {
    private Connection connection;
    public IServerSoundSystem(Connection connection) {
        PacketListener.addListener(
                new IPacketListener((packet) -> {
                    if (packet instanceof ServerPlaySoundPacket) {
                        try {
                            Clip clip = AudioSystem.getClip();
                            clip.open(((ServerPlaySoundPacket) packet).getData().getInputStream());
                            clip.start();
                        } catch (LineUnavailableException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
        );
        setConnection(connection);
    }

    public void playSound(File file) throws UnsupportedAudioFileException, IOException {
        if (getConnection().getServer().getAddress().matchIp(Machine.localIpAddress)) {
            try {
                Clip clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(file));
                clip.start();
            } catch (LineUnavailableException | IOException e) {
                e.printStackTrace();
            }
        } else if (getConnection().getClient().getAddress().matchIp(Machine.localIpAddress)) {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
            PlaySoundRequest request = new PlaySoundRequest(inputStream);
            ServerPlaySoundPacket packet = new ServerPlaySoundPacket(request);
            packet.send(getConnection());
        } else {
            throw new RuntimeException("A third party connection is trying to access the system.");
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    private void setConnection(Connection connection) {
        this.connection = connection;
    }
}
