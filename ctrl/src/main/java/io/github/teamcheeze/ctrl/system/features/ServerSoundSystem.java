package io.github.teamcheeze.ctrl.system.features;

import javazoom.jl.player.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public interface ServerSoundSystem extends ServerSideSystem {
    List<Player> getPlayers();

    void playSound(File file) throws FileNotFoundException;
}
