package io.github.teamcheeze.remoteActions.network.connection;

import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.Packet;
import io.github.teamcheeze.remoteActions.network.PacketData;
import io.github.teamcheeze.remoteActions.network.data.packets.GoodbyePacket;
import io.github.teamcheeze.remoteActions.network.data.server.GoodbyeRequest;
import io.github.teamcheeze.remoteActions.server.Server;
import io.github.teamcheeze.remoteActions.server.features.*;
import io.github.teamcheeze.remoteActions.server.fs.IServerFileSystem;
import io.github.teamcheeze.remoteActions.server.io.IServerKeyboardSystem;
import io.github.teamcheeze.remoteActions.server.io.IServerMouseSystem;
import io.github.teamcheeze.remoteActions.server.media.IServerSoundSystem;
import io.github.teamcheeze.remoteActions.server.os.IServerTasksSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

/**
 * Connection should always be accepted by the server and be sent by the server.
 */
public class IConnection implements Connection {
    private Client client;
    private Server server;
    private transient ServerSocket serverSocket;
    private boolean valid;
    private final ServerFileSystem fs;
    private final ServerKeyboardSystem keyboard;
    private final ServerMouseSystem mouse;
    private final ServerSoundSystem sound;
    private final ServerTasksSystem tasks;
    public IConnection() {
        this.fs = new IServerFileSystem(this);
        this.keyboard = new IServerKeyboardSystem(this);
        this.mouse = new IServerMouseSystem(this);
        this.sound = new IServerSoundSystem(this);
        this.tasks = new IServerTasksSystem(this);
        setValid(true);
    }
    public IConnection(@NotNull UUID clientId, @NotNull UUID hostId) {
        this();
        this.client = IConnectionHandler.getClient(clientId);
        this.server = IConnectionHandler.getServer(hostId);
    }

    public IConnection(@Nullable Client client, @Nullable Server server) {
        this();
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
    public void abort() {
        validate();
        GoodbyeRequest request = new GoodbyeRequest(this);
        GoodbyePacket packet = new GoodbyePacket(request);
        sendPacket(packet);
        setValid(false);
        if (!packet.getData().isSuccess()) {
            throw new RuntimeException("Error occurred while invalidating the connection");
        }
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
        validate();
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
            return packet;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ServerFileSystem fs() {
        validate();
        return fs;
    }

    @Override
    public ServerKeyboardSystem keyboard() {
        validate();
        return keyboard;
    }

    @Override
    public ServerMouseSystem mouse() {
        validate();
        return mouse;
    }

    @Override
    public ServerSoundSystem sound() {
        validate();
        return sound;
    }

    @Override
    public ServerTasksSystem tasks() {
        validate();
        return tasks;
    }

    @Override
    public boolean isValid() {
        return this.valid;
    }

    private void setValid(boolean valid) {
        this.valid = valid;
    }
    @Override
    public void validate() {
        if (!isValid()) {
            throw new RuntimeException("Trying to use an invalidated connection.");
        }
    }
}
