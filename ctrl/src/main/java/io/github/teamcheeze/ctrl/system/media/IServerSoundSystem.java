package io.github.teamcheeze.ctrl.system.media;

import io.github.teamcheeze.ctrl.packets.internal.ServerPlaySoundPacket;
import io.github.teamcheeze.ctrl.system.features.ServerSoundSystem;
import io.github.teamcheeze.remoteActions.network.Connection;
import javazoom.jl.player.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class IServerSoundSystem implements ServerSoundSystem {
    private Connection connection;
    private final List<Player> players = new ArrayList<>();
    public IServerSoundSystem(Connection connection) {
        connection.validate();
        this.connection = connection;
    }

    @Override
    public List<Player> getPlayers() {
        connection.validate();
        return players;
    }

    @Override
    public void playSound(File file) {
        connection.validate();
        ServerPlaySoundPacket packet = new ServerPlaySoundPacket(file);
        connection.sendPacket(packet);
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    private void setConnection(Connection connection) {
        this.connection = connection;
    }
}
