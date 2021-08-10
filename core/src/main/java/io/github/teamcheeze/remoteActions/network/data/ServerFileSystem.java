package io.github.teamcheeze.remoteActions.network.data;

import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.data.fs.FileReadRequest;
import io.github.teamcheeze.remoteActions.network.data.packets.IPacketListener;
import io.github.teamcheeze.remoteActions.network.data.packets.ServerFileReadPacket;
import io.github.teamcheeze.remoteActions.util.Machine;

import java.io.File;

public class ServerFileSystem {
    private final Connection connection;
    public ServerFileSystem(Connection connection) {
        this.connection = connection;
        PacketListener.addListener(
                new IPacketListener(object -> {
                    if (object instanceof ServerFileReadPacket) {
                        FileReadRequest request = ((ServerFileReadPacket) object).getData();
                        if (request.getServer().getAddress().matchIp(Machine.localIpAddress)) {
                            request.setResult(new File(request.getRequestedFilename()));
                        }
                    }
                })
        );
    }
    public File getFile(String filename) {
        if (connection.getServer().getAddress().matchIp(Machine.localIpAddress)) {
            // Check if the current system is the server
            // File handling actions
            return new File(filename);
        } else if (connection.getClient().getAddress().matchIp(Machine.localIpAddress)) {
            // The current system is the client of the connection
            FileReadRequest readRequest = new FileReadRequest(connection, filename);
            ServerFileReadPacket packet = new ServerFileReadPacket(readRequest);
            connection.getClient().sendPacket(connection, packet);
            return readRequest.getResult();
        } else {
            throw new RuntimeException("A third party client is trying to intercept a connection!! Cracking?");
        }
    }
}
