package io.github.teamcheeze.remoteActions.server;

import io.github.dolphin2410.jaw.util.async.Async;
import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.connection.IConnectionHandler;
import io.github.teamcheeze.remoteActions.network.connection.ServerSocketThread;
import io.github.teamcheeze.remoteActions.network.server.ServerAddress;
import io.github.teamcheeze.remoteActions.util.Cancellable;
import io.github.teamcheeze.remoteActions.util.Machine;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IServer implements Server, Cancellable {
    private boolean canceled = false;
    private final UUID id = UUID.randomUUID();
    private final ServerAddress address;
    private boolean initialized = false;
    private boolean valid;
    private final List<Connection> connections = new ArrayList<>();

    public IServer(int port) {
        this.valid = true;
        this.address = new ServerAddress(Machine.localIpAddress, port);
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public ServerConfigurations config = new ServerConfigurations();
    // TODO Initialize config file

    @Override
    public @NotNull UUID getId() {
        return id;
    }

    @NotNull
    @Override
    public ServerAddress getAddress() {
        return address;
    }

    @Override
    public void initialize() {
        if (initialized) {
            throw new RuntimeException("Cannot re initialize a server.");
        }
        ServerSocket tempServerSocket;
        try {
            tempServerSocket = new ServerSocket(address.port);
        } catch (IOException e) {
            try {
                tempServerSocket = new ServerSocket(0);
            } catch (IOException ex) {
                e.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
        final ServerSocket serverSocket = tempServerSocket;
        initialized = true;
        System.out.println("Local server hosted on port: " + getAddress().getPort() + " [ip: " + getAddress().getIp().getHostAddress() + "]");
        Async.execute(() -> {
            while (!isCanceled()) {
                try {
                    Socket receivedSocket = serverSocket.accept();
                    InetAddress remoteAddress = ((InetSocketAddress) receivedSocket.getRemoteSocketAddress()).getAddress();
                    System.out.println("[Server] Connection request established on " + remoteAddress.getHostAddress() + ".");
                    new ServerSocketThread(IConnectionHandler.getClient(remoteAddress), this, serverSocket, receivedSocket).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            setCanceled(true);
            try {
                for (Connection connection : connections) {
                    connection.abort();
                }
                serverSocket.close();
                System.out.println("Server closing safely..");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public List<Connection> getConnections() {
        return connections;
    }
}
