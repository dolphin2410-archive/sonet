package io.github.teamcheeze.remoteActions.network.data;

import io.github.teamcheeze.remoteActions.network.Address;
import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.data.fs.FileReadRequest;
import io.github.teamcheeze.remoteActions.network.data.packets.IPacketListener;
import io.github.teamcheeze.remoteActions.network.data.packets.ServerFileReadPacket;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerFileSystem {
    private final Connection connection;
    public ServerFileSystem(Connection connection) {
        this.connection = connection;
        PacketListener.addListener(
                new IPacketListener(object -> {
                    if (object instanceof ServerFileReadPacket) {
                        FileReadRequest request = ((ServerFileReadPacket) object).getData();
                        if (request)
                    }
                })
        );
    }
    public File getFile(String filename) throws UnknownHostException {
        Address systemAddress = new Address((Inet4Address) InetAddress.getLocalHost(), 10);
        if (connection.getServer().getAddress().equals(systemAddress)) {
            // Check if the current system is the server
            // File handling actions

        } else if (connection.getClient().getAddress().equals(systemAddress)) {
            // The current system is the client of the connection

        } else {
            throw new RuntimeException("A third party client is trying to intercept a connection!! Cracking?");
        }
    }
}
