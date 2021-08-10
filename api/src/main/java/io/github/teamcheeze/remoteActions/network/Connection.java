package io.github.teamcheeze.remoteActions.network;

import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.server.Server;
import org.jetbrains.annotations.NotNull;
import java.net.ServerSocket;

public interface Connection {
    @NotNull Client getClient();
    @NotNull Server getServer();
    @NotNull ServerSocket getServerSocket();
    <R extends PacketData, T extends Packet<R>> T sendPacket(T packet);
}
