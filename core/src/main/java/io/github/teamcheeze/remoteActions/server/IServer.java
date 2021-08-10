package io.github.teamcheeze.remoteActions.server;

import io.github.dolphin2410.jaw.util.async.Async;
import io.github.teamcheeze.remoteActions.network.Address;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class IServer implements Server {
    private final UUID id;
    private Address address;
    private ServerSocket serverSocket;
    public IServer() {
        this.id = UUID.randomUUID();
    }
    public IServer(Address address) {
        this.id = UUID.randomUUID();
        this.address = address;
    }

    public ServerConfigurations config = new ServerConfigurations();
    // TODO Initialize file

    @Override
    public @NotNull UUID getId() {
        return id;
    }

    @NotNull
    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void initialize() {
        try {
            serverSocket = new ServerSocket(address.port);
            while (true) {
                Socket receivedSocket = serverSocket.accept();
                new Thread(()->{

                }).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
