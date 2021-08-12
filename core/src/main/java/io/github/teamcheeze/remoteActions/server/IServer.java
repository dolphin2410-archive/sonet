package io.github.teamcheeze.remoteActions.server;

import io.github.dolphin2410.jaw.util.async.Async;
import io.github.teamcheeze.remoteActions.network.server.ServerAddress;
import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.connection.IConnectionHandler;
import io.github.teamcheeze.remoteActions.network.connection.ServerSocketThread;
import io.github.teamcheeze.remoteActions.server.fs.IServerFileSystem;
import io.github.teamcheeze.remoteActions.util.Cancellable;
import io.github.teamcheeze.remoteActions.util.Machine;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class IServer implements Server, Cancellable {
    private boolean canceled = false;
    private final UUID id = UUID.randomUUID();
    private ServerAddress address;
    private transient ServerSocket serverSocket;
    private boolean initialized = false;
    private boolean debug = true;

    public IServer() {
    }

    public IServer(ServerAddress address) {
        this.address = address;
    }

    public IServer(int port) {
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

    public IServerFileSystem getServerFileSystem(Connection connection) {
        return new IServerFileSystem(connection);
    }

    @Override
    public void initialize() {
        if (initialized) {
            throw new RuntimeException("Cannot re initialize a server.");
        }
        try {
            serverSocket = new ServerSocket(address.port);
        } catch (IOException e) {
            try {
                serverSocket = new ServerSocket(0);
                debugMsg("The port has moved from " + address.port + " to " + serverSocket.getLocalPort() + " because the port was already used");
            } catch (IOException ex) {
                e.printStackTrace();
            }
        }
        initialized = true;
        Async.execute(() -> {
            while (!isCanceled()) {
                try {
                    Socket receivedSocket = serverSocket.accept();
                    Inet4Address remoteAddress = (Inet4Address) ((InetSocketAddress) receivedSocket.getRemoteSocketAddress()).getAddress();
                    System.out.println("[Server] Connection request established on " + remoteAddress.getHostAddress() + ".");
                    new ServerSocketThread(IConnectionHandler.getClient(remoteAddress), this, serverSocket, receivedSocket).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        while (!isCanceled()) {
            String input = System.console().readLine();
            if (input.equalsIgnoreCase("stop")) {
                setCanceled(true);
            }
        }
    }

    private void debugMsg(Object obj) {
        if (isDebug()) {
            System.out.println(obj);
        }
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @Override
    public ServerSocket getServerSocket() {
        return serverSocket;
    }
}
