package io.github.teamcheeze.remoteActions.network.data.fs;

import io.github.teamcheeze.remoteActions.network.data.packets.ServerFileReadPacket;
import io.github.teamcheeze.remoteActions.server.Server;

public class FileReadRequest {
    private Server server;
    public FileReadRequest() {

    }
    public FileReadRequest(Server server) {
        this.server = server;
    }

}
