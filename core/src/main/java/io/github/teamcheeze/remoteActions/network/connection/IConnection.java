package io.github.teamcheeze.remoteActions.network.connection;

import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.Packet;
import io.github.teamcheeze.remoteActions.network.PacketData;
import io.github.teamcheeze.remoteActions.server.Server;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class IConnection implements Connection {
    private Client client;
    private Server server;
    private ServerSocket serverSocket;
    public IConnection() {

    }
    public IConnection(@NotNull UUID clientId, @NotNull UUID hostId) {
        this.client = IConnectionHandler.getClient(clientId);
        this.server = IConnectionHandler.getServer(hostId);
    }

    public IConnection(@Nullable Client client, @Nullable Server server) {
        this.client = client;
        this.server = server;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @NotNull
    @Override
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    @Override
    @NotNull
    public Client getClient() {
        return client;
    }

    @NotNull
    @Override
    public Server getServer() {
        return server;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends PacketData, T extends Packet<R>> T sendPacket(T packet) {
        try {
            System.out.println("[IClient::sendPacket]Packet sent");
            Socket clientSocket = new Socket(getServer().getAddress().ip, getServer().getAddress().port);
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            outputStream.writeObject(packet);
            outputStream.flush();
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            T returnedPacket = (T) inputStream.readObject();
            packet.updateData(returnedPacket);
            clientSocket.close();
            // ??E r r o r??
            return packet;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
