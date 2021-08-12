package io.github.teamcheeze.remoteActions.network.data;

import io.github.dolphin2410.jaw.util.async.Async;
import io.github.dolphin2410.jaw.util.kotlin.KWrapper;
import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.network.Packet;
import io.github.teamcheeze.remoteActions.network.connection.IConnection;
import io.github.teamcheeze.remoteActions.network.connection.IConnectionHandler;
import io.github.teamcheeze.remoteActions.network.connection.ServerSocketThread;
import io.github.teamcheeze.remoteActions.network.data.fs.FileReadRequest;
import io.github.teamcheeze.remoteActions.network.data.packets.GoodbyePacket;
import io.github.teamcheeze.remoteActions.network.data.packets.HelloPacket;
import io.github.teamcheeze.remoteActions.network.data.packets.KeyPressPacket;
import io.github.teamcheeze.remoteActions.network.data.packets.ServerFileReadPacket;
import io.github.teamcheeze.remoteActions.server.KeyboardKeys;
import io.github.teamcheeze.remoteActions.util.Machine;
import io.github.teamcheeze.remoteActions.util.RemoteFile;

import java.awt.*;
import java.io.File;
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

    public static void handleDefault(ServerSocketThread thread, Packet<?> packet) {
        if (packet instanceof HelloPacket serverIdentityPacket) {
            serverIdentityPacket.getData().setServer(thread.getServer());
            Client newClient = serverIdentityPacket.getData().getClient();
            IConnection newConnection = new IConnection(newClient, thread.getServer());
            newConnection.setServerSocket(thread.getServerSocket());
            serverIdentityPacket.getData().setConnection(newConnection);
            IConnectionHandler.registerClient(newClient);
            System.out.println("Registered a new client. IP: " + newClient.getAddress().getIp().getHostAddress() + " UUID: " + newClient.getId());
        } else {
            if (thread.getClient() == null) {
                throw new NullPointerException("The client instance cannot be null");
            }
        }
        if (packet instanceof GoodbyePacket goodbyePacket) {
            goodbyePacket.getData().goodbye();
            goodbyePacket.getData().setSuccess(true);
        }
        if (packet instanceof ServerFileReadPacket frpacket) {
            frpacket.setFile(new RemoteFile(new File(frpacket.getRequestedFilename())));
        }
        if (packet instanceof KeyPressPacket) {
            Async.execute(()->{
                try {
                    Robot robot = new Robot();
                    for (KeyboardKeys key : ((KeyPressPacket) packet).getKeys()) {
                        robot.keyPress(key.getKeycode());
                    }
                    Thread.sleep(((KeyPressPacket) packet).getDuration());
                    for (KeyboardKeys key : ((KeyPressPacket) packet).getKeys()) {
                        robot.keyRelease(key.getKeycode());
                    }
                } catch (AWTException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void onReceived(Object object) {
    }
}
