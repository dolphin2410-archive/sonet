package io.github.teamcheeze.remoteActions.network.data.media;

import io.github.teamcheeze.remoteActions.network.PacketData;

import javax.sound.sampled.AudioInputStream;

public class PlaySoundRequest implements PacketData {
    private AudioInputStream inputStream;
    public PlaySoundRequest() {

    }
    public PlaySoundRequest(AudioInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public AudioInputStream getInputStream() {
        return inputStream;
    }
}
