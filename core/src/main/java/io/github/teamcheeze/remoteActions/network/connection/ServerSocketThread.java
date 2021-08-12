package io.github.teamcheeze.remoteActions.network.connection;

import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.network.data.packets.GoodbyePacket;
import io.github.teamcheeze.remoteActions.network.server.ServerAddress;
import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.Packet;
import io.github.teamcheeze.remoteActions.network.data.PacketListener;
import io.github.teamcheeze.remoteActions.network.data.packets.HelloPacket;
import io.github.teamcheeze.remoteActions.server.Server;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    private final ServerAddress socketClientAddress;
    private final ServerAddress socketServerAddress;

    public ServerSocketThread(@Nullable Client client, Server server, ServerSocket serverSocket, Socket socket) throws IOException {
        super(() -> {

        });
        this.serverSocket = serverSocket;
        this.socket = socket;
        InetSocketAddress remoteClientAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
        Inet4Address clientIpv4 = (Inet4Address)remoteClientAddress.getAddress();
        ServerAddress clientAddress = new ServerAddress(clientIpv4, remoteClientAddress.getPort());
        InetSocketAddress remoteAddress = (InetSocketAddress) serverSocket.getLocalSocketAddress();
        Inet4Address serverIpv4 = (Inet4Address)remoteAddress.getAddress();
        ServerAddress serverAddress = new ServerAddress(serverIpv4, remoteAddress.getPort());
        this.client = client;
        this.connection = new IConnection(client, server);
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
                System.out.println(packet.getClass());
                PacketListener.getListeners().forEach(it -> it.onReceived(object));
                // Call the packet listeners
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                PacketListener.handleDefault(this, packet);
                outputStream.writeObject(packet);
                outputStream.flush();
            } catch (IOException | ClassNotFoundException | ClassCastException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    public Server getServer() {
        return server;
    }

    public Socket getSocket() {
        return socket;
    }

    public ServerAddress getSocketClientAddress() {
        return socketClientAddress;
    }
}
