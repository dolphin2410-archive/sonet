package io.github.teamcheeze.remoteActions.network.connection;

import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.Packet;
import io.github.teamcheeze.remoteActions.network.client.ClientAddress;
import io.github.teamcheeze.remoteActions.network.data.GoodbyePacket;
import io.github.teamcheeze.remoteActions.network.data.PacketListener;
import io.github.teamcheeze.remoteActions.network.server.ServerAddress;
import io.github.teamcheeze.remoteActions.server.IServer;
import io.github.teamcheeze.remoteActions.server.Server;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerSocketThread extends Thread {
    private final Client client;
    private final Server server;
    private Connection connection;
    private final ServerSocket serverSocket;
    private final Socket socket;
    private final ClientAddress socketClientAddress;
    private final ServerAddress socketServerAddress;

    public ServerSocketThread(@Nullable Client client, Server server, ServerSocket serverSocket, Socket socket) throws IOException {
        super(() -> {

        });
        this.serverSocket = serverSocket;
        this.socket = socket;
        InetSocketAddress remoteClientAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
        Inet4Address clientIpv4 = (Inet4Address)remoteClientAddress.getAddress();
        ClientAddress clientAddress = new ClientAddress(clientIpv4);
        InetSocketAddress remoteAddress = (InetSocketAddress) serverSocket.getLocalSocketAddress();
        Inet4Address serverIpv4 = (Inet4Address)remoteAddress.getAddress();
        ServerAddress serverAddress = new ServerAddress(serverIpv4, remoteAddress.getPort());
        if (client != null) {
            List<Connection> connections = new ArrayList<>(((IServer) server).getConnections().stream().filter(it -> it.getClient() == client).toList());
            if (connections.size() != 1) {
                for (Connection connection : connections) {
                    connection.abort();
                }
                connections.clear();
                throw new RuntimeException("Multiple or no client(s) sharing one connection.");
            }
            this.connection = connections.get(0);
        }
        this.client = client;
        this.server = server;
        this.socketClientAddress = clientAddress;
        this.socketServerAddress = serverAddress;
    }

    @Override
    public synchronized void start() {
        validate(() -> {
            try {
                while (((connection == null && client == null) || (connection != null && connection.isValid())) && socket.isConnected() && !socket.isClosed()) {
                    InputStream is = socket.getInputStream();
                    ObjectInputStream inputStream = new ObjectInputStream(is);
                    Packet<?> packet = (Packet<?>) inputStream.readObject();
                    Object object = packet.getData();
                    System.out.println("[Incoming Packet]: " + packet.getClass().getSimpleName());
                    PacketListener.getListeners().forEach(it -> it.onReceived(object));
                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    PacketListener.handleDefault(this, packet);
                    outputStream.writeObject(packet);
                    outputStream.flush();
                    if (packet instanceof GoodbyePacket) {
                        break;
                    }
                    System.out.println("[Outgoing Packet]: " + packet.getClass().getSimpleName());
                }
            } catch (IOException | ClassNotFoundException | ClassCastException e) {
                e.printStackTrace();
            }
        });
    }

    private boolean isValid() {
//        if (!getSocketClientAddress().matchIp(getClient().getAddress())) {
//            return false;
//        }
//        if (!getSocketServerAddress().matchIp(getServer().getAddress())) {
//            return false;
//        }
        return true;
    }
    private void validate(Runnable runnable) {
        if (isValid()) {
            runnable.run();
        }
    }

    public ServerAddress getSocketServerAddress() {
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

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Server getServer() {
        return server;
    }

    public Socket getSocket() {
        return socket;
    }

    public ClientAddress getSocketClientAddress() {
        return socketClientAddress;
    }
}
