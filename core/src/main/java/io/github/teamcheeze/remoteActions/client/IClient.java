package io.github.teamcheeze.remoteActions.client;

import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.Packet;
import io.github.teamcheeze.remoteActions.network.PacketData;
import io.github.teamcheeze.remoteActions.network.client.ClientAddress;
import io.github.teamcheeze.remoteActions.network.connection.IConnection;
import io.github.teamcheeze.remoteActions.network.connection.IConnectionHandler;
import io.github.teamcheeze.remoteActions.network.data.HelloPacket;
import io.github.teamcheeze.remoteActions.util.Listener;
import io.github.teamcheeze.remoteActions.util.Machine;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class IClient implements Client {
    private final UUID id = UUID.randomUUID();
    private final ClientAddress address;
    private Connection connection;
    private boolean valid;
    private Map<Packet<?>, CompletableFuture<Packet<?>>> packets = new HashMap<>();
    private transient Listener<Packet<?>> listener;

    public IClient() {
        this.valid = true;
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
        try {
            // Say hello to server
            // Since connection is not set yet, must be sent manually
            Socket clientSocket = new Socket(ip, port);
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            HelloPacket packet = new HelloPacket(this);
            outputStream.writeObject(packet);
            outputStream.flush();
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            HelloPacket returnedPacket = (HelloPacket) inputStream.readObject();
            IConnectionHandler.registerServer(returnedPacket.getData().getServer());
            // Update the client's ip returned by the connection.
            this.address.setIp(returnedPacket.getData().getClient().getAddress().getIp());
            connection = returnedPacket.getData().getConnection();
            listener = new Listener<>() {
                @Override
                @SuppressWarnings("unchecked")
                public void onAction(Packet<?> obj) {
                    try {
                        if (connection.isValid() && !clientSocket.isClosed() && clientSocket.isConnected()) {
                            ObjectOutputStream objOutStream = new ObjectOutputStream(clientSocket.getOutputStream());
                            System.out.println(obj.getClass());
                            objOutStream.writeObject(obj);
                            objOutStream.flush();
                            ObjectInputStream objInStream = new ObjectInputStream(clientSocket.getInputStream());
                            Packet<?> newObject = (Packet<?>) objInStream.readObject();
                            obj.updateData(obj.getClass().cast(newObject));
                        }
                    } catch (IOException | ClassNotFoundException | ClassCastException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            };
            ((IConnection) connection).setListener(listener);
            System.out.println("Listener created");
            return connection;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid Status. Connection Refused.");
        }
    }

    public void initialize() {

    }

    /**
     * @param <T> The type of the packet
     * @param packet The packet to send
     * @return The updated packet ( self )
     */
    @Override
    public <R extends PacketData, T extends Packet<R>> T sendPacket(T packet) {
        if (listener == null) {
            throw new RuntimeException("The client hasn't been connected");
        }
        listener.onAction(packet);
        return packet;
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

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void abort() {
        connection.abort();
    }
}
