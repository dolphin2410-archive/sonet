package io.github.teamcheeze.remoteActions;

import io.github.teamcheeze.remoteActions.network.data.PacketListener;
import io.github.teamcheeze.remoteActions.network.data.packets.IPacketListener;
import io.github.teamcheeze.remoteActions.network.data.packets.ServerIdentityPacket;

public class Initializer {
    public static void initialize() {
        RemoteActions.setSupplier(new IRemoteActionsSupplier());
    }
}
