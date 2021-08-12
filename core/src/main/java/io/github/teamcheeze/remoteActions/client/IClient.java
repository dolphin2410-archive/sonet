package io.github.teamcheeze.remoteActions.client;

import io.github.teamcheeze.remoteActions.network.*;
import io.github.teamcheeze.remoteActions.network.client.ClientAddress;
import io.github.teamcheeze.remoteActions.network.connection.IConnectionHandler;
import io.github.teamcheeze.remoteActions.network.data.packets.HelloPacket;
import io.github.teamcheeze.remoteActions.network.data.server.HelloRequest;
import io.github.teamcheeze.remoteActions.util.Machine;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;

public class IClient implements Client {
    private final UUID id = UUID.randomUUID();
    private final ClientAddress address;

    /**
     * Use this constructor if
     */
    public IClient(ClientAddress address) {
        this.address = address;
    }

    public IClient() {
        this.address = new ClientAddress(Machine.localIpAddress);
    }

    /**
     * Creates a connection to the server over socket
     * @param ip The ip of the server
     * @param port The port of the server
     * @return The created connection.
     */
    @Override
    public @NotNull Connection connect(@NotNull InetAddress ip, int port) {
        return requestServerIdentity(ip, port);
    }

    /**
     * Make sure to call this before using IClient(id, id). This requests identity to the server, while also registering the client to the server.
     * @param ipv4 The server's ipv4
     * @param port The server's port
     * @return The server instance
     */
    @NotNull
    private Connection requestServerIdentity(InetAddress ipv4, int port) {
        try {
            // Say hello to server
            // Since connection is not set yet, must be sent manually
            Socket clientSocket = new Socket(ipv4, port);
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            HelloRequest request = new HelloRequest(this);
            HelloPacket packet = new HelloPacket(request);
            outputStream.writeObject(packet);
            outputStream.flush();
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            HelloPacket returnedPacket = (HelloPacket) inputStream.readObject();
            clientSocket.close();
            IConnectionHandler.registerServer(returnedPacket.getData().getServer());
            return returnedPacket.getData().getConnection();
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("The server didn't return any instance");
            throw new RuntimeException("Invalid Status. Connection Aborted.");
        }
    }

    public void initialize() {

    }

    /**
     * @param <T> The type of the packet
     * @param connection The connection between the client and server
     * @param packet The packet to send
     * @return The updated packet ( self )
     */
    @Override
    public <R extends PacketData, T extends Packet<R>> T sendPacket(Connection connection, T packet) {
        return connection.sendPacket(packet);
    }

    @Override
    public @NotNull UUID getId() {
        return id;
    }

    @NotNull
    @Override
    public ClientAddress getAddress() {
        return address;
    }
}
