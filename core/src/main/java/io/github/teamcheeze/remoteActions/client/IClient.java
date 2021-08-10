package io.github.teamcheeze.remoteActions.client;

import io.github.teamcheeze.remoteActions.network.*;
import io.github.teamcheeze.remoteActions.network.connection.IConnection;
import io.github.teamcheeze.remoteActions.network.data.IPacket;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.UUID;

public class IClient implements Client {
    private final UUID id;
    private Address address;
    private Socket clientSocket;

    public IClient() {
        this.id = UUID.randomUUID();
    }

    public IClient(Address address) {
        this.id = UUID.randomUUID();
        this.address = address;
    }

    @Override
    public @NotNull Connection connect(@NotNull Inet4Address ipv4, int port) throws IOException {
        clientSocket = new Socket(ipv4, port);
        ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        outputStream.writeObject(new IPacket<>());
        return new IConnection(this.getId(), this.getId());
    }

    @Override
    public void sendPacket(Packet<?> packet) {

    }

    @Override
    public @NotNull UUID getId() {
        return id;
    }

    @NotNull
    @Override
    public Address getAddress() {
        return address;
    }
}
