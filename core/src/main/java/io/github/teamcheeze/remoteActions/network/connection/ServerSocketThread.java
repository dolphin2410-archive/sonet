package io.github.teamcheeze.remoteActions.network.connection;

import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.network.Address;
import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.Packet;
import io.github.teamcheeze.remoteActions.network.connection.IConnection;
import io.github.teamcheeze.remoteActions.network.data.PacketListener;
import io.github.teamcheeze.remoteActions.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketThread extends Thread {
    private final Client client;
    private final Server server;
    private final Connection connection;
    private final Socket socket;
    private final ServerSocket serverSocket;
    private final Address socketClientAddress;
    private final Address socketServerAddress;

    public ServerSocketThread(Client client, Server server, ServerSocket serverSocket) throws IOException {
        super(() -> {

        });
        this.serverSocket = serverSocket;
        this.socket = serverSocket.accept();
        InetSocketAddress remoteClientAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
        Inet4Address clientIpv4 = (Inet4Address)remoteClientAddress.getAddress();
        Address clientAddress = new Address(clientIpv4, remoteClientAddress.getPort());
        InetSocketAddress remoteAddress = (InetSocketAddress) serverSocket.getLocalSocketAddress();
        Inet4Address serverIpv4 = (Inet4Address)remoteAddress.getAddress();
        Address serverAddress = new Address(serverIpv4, remoteAddress.getPort());
        this.client = client;
        this.connection = new IConnection(client.getId(), server.getId());
        this.server = server;
        this.socketClientAddress = clientAddress;
        this.socketServerAddress = serverAddress;
    }

    @Override
    public synchronized void start() {
        validate(() -> {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                // input stream is what the client requested
                Packet<?> packet = (Packet<?>) inputStream.readObject();
                // The requested class might not be found or might not be a packet
                Object object = packet.getData();
                PacketListener.getListeners().forEach(it -> it.onReceived(object));
                // Call the packet listeners
                socket.close();
            } catch (IOException | ClassNotFoundException | ClassCastException e) {
                e.printStackTrace();
            }
        });
    }

    private boolean isValid() {
        if (getSocketClientAddress() != getClient().getAddress()) {
            return false;
        }
        if (getSocketServerAddress() != getServer().getAddress()) {
            return false;
        }
        return true;
    }
    private void validate(Runnable runnable) {
        if (isValid()) {
            runnable.run();
        }
    }

    public Address getSocketServerAddress() {
        return socketServerAddress;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public Client getClient() {
        return client;
    }

    public Connection getConnection() {
        return connection;
    }

    public Server getServer() {
        return server;
    }

    public Socket getSocket() {
        return socket;
    }

    public Address getSocketClientAddress() {
        return socketClientAddress;
    }
}
