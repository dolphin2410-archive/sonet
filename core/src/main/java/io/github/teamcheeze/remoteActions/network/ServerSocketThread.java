package io.github.teamcheeze.remoteActions.network;

import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.server.Server;

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ServerSocketThread extends Thread {
    private final Client client;
    private final Server server;
    private final Connection connection;
    private final Socket socket;
    private final Address socketClientAddress;
    public ServerSocketThread(Client client, Server server, Socket socket) {
        super(() -> {

        });
        InetSocketAddress remoteAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
        Inet4Address ipv4 = (Inet4Address)remoteAddress.getAddress();
        Address clientAddress = new Address(ipv4, remoteAddress.getPort());
        this.client = client;
        this.connection = new IConnection(client.getId(), server.getId());
        this.server = server;
        this.socket = socket;
        this.socketClientAddress = clientAddress;
    }

    @Override
    public synchronized void start() {
        // Action to control
    }

    private boolean isValid() {
        if (getSocketClientAddress() != getClient().getAddress()) {
            return false;
        }
        return true;
    }
    private void validate(Runnable runnable) {
        if (isValid()) {
            runnable.run();
        }
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
