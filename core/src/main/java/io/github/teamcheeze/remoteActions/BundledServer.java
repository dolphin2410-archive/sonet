package io.github.teamcheeze.remoteActions;

import io.github.dolphin2410.jaw.util.io.LocalFile;
import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.server.IServer;
import io.github.teamcheeze.remoteActions.server.Server;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class BundledServer {
    public static void main(String[] args) throws IOException {
        Initializer.initialize();
        Client client = RemoteActions.registerClient();
        System.out.println("Client hosted on port 8080");
        Server server = RemoteActions.hostServer(9090);
        System.out.println("Server hosted on port 9090");
        Connection connection = client.connect(server.getAddress());
        // Stuck here
        System.out.println("Connection complete");
        File file = ((IServer) server).getServerFileSystem(connection).getFile("C:\\Users\\dolph\\Documents\\JmFiles\\PlumJuice\\build\\libs\\hello.txt");
        System.out.println("Received file");
        System.out.println(new LocalFile(file).readAllContents());
        JFrame frame = new JFrame("Clients");
        frame.setSize(192 * 5, 108 * 5);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        System.out.println("Hi!!!");
    }
}
