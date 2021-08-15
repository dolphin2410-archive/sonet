package io.github.teamcheeze.remoteActions;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.io.IOException;

public class MediaTest {
    public static void main(String[] args) throws IOException, JavaLayerException {
        new Player(new FileInputStream("tumble.mp3")).play();
    }
}