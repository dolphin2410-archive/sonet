package io.github.teamcheeze.remoteActions.network.data;

import io.github.dolphin2410.jaw.util.kotlin.KWrapper;
import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.Packet;
import io.github.teamcheeze.remoteActions.network.connection.IConnection;
import io.github.teamcheeze.remoteActions.network.connection.IConnectionHandler;
import io.github.teamcheeze.remoteActions.network.connection.ServerSocketThread;
import io.github.teamcheeze.remoteActions.server.IServer;

import java.util.ArrayList;

public abstract class PacketListener {
    private static final ArrayList<PacketListener> listeners = new ArrayList<>();
    public static PacketListener addListener(PacketListener listener) {
        return KWrapper.apply(listener, listeners::add);
    }

    public static void removeListener(PacketListener listener) {
        listeners.remove(listener);
    }

    public static ArrayList<PacketListener> getListeners() {
        return listeners;
    }

    public void onReceived(Object object) {
    }
    public static void handleDefault(ServerSocketThread thread, Packet<?> packet) {
        if (packet instanceof HelloPacket) {
            Client client = ((HelloPacket) packet).getClient();
            IConnectionHandler.registerClient(client);
            ((HelloPacket) packet).setServer(thread.getServer());
            Connection connection = new IConnection(client, thread.getServer());
            ((HelloPacket) packet).setConnection(connection);
            ((IServer) thread.getServer()).getConnections().add(connection);
        }
        if (packet instanceof GoodbyePacket) {
            ((GoodbyePacket) packet).goodbye();
            System.out.println("Disconnected successfully");
        }
    }
}
