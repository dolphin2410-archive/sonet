package io.github.teamcheeze.ctrl;

import io.github.dolphin2410.jaw.util.async.Async;
import io.github.teamcheeze.ctrl.packets.internal.IPacketListener;
import io.github.teamcheeze.ctrl.packets.internal.KeyPressPacket;
import io.github.teamcheeze.ctrl.packets.internal.ServerFileReadPacket;
import io.github.teamcheeze.ctrl.packets.internal.ServerPlaySoundPacket;
import io.github.teamcheeze.remoteActions.Initializer;
import io.github.teamcheeze.remoteActions.RemoteActions;
import io.github.teamcheeze.remoteActions.network.data.PacketListener;
import io.github.teamcheeze.remoteActions.server.KeyboardKeys;
import io.github.teamcheeze.remoteActions.server.Server;
import io.github.teamcheeze.remoteActions.util.RemoteFile;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.awt.*;
import java.io.File;

public class CtrlServer {
    public static void main(String[] args) {
        Initializer.initialize();
        Server server = RemoteActions.hostServer(44444);
        PacketListener.addListener(new IPacketListener((packet)->{
            if (packet instanceof ServerFileReadPacket SFRP) {
                SFRP.setFile(new RemoteFile(new File(SFRP.getRequestedFilename())));
            }
            if (packet instanceof ServerPlaySoundPacket SPSP) {
                Async.execute(() -> {
                    try {
                        new Player(SPSP.getInputStream()).play();
                    } catch (JavaLayerException e) {
                        e.printStackTrace();
                    }
                });
            }
            if (packet instanceof KeyPressPacket KPP) {
            }
        }));
    }
}
