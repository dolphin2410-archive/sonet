package io.github.teamcheeze.remoteActions.client;

import io.github.teamcheeze.remoteActions.Initializer;
import io.github.teamcheeze.remoteActions.RemoteActions;
import io.github.teamcheeze.remoteActions.network.client.ClientAddress;
import io.github.teamcheeze.remoteActions.network.server.ServerAddress;
import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.util.Machine;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;

public class ClientApplication {
    private static GridBagLayout layout;
    private Connection connection;
    public static void main(String[] args) throws IOException {
//        connect(10101);
        Initializer.initialize();
        Client client = RemoteActions.registerClient();
        new ClientApplication().createFrame(client);
    }

    public boolean isConnected() {
        return connection != null;
    }

    // Source: https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=javaking75&logNo=140189054193
    public void addComponentGBag(Frame parent, Component c, int x, int y, int w, int h){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        layout.setConstraints(c,gbc);
        parent.add(c);
    }

    public JPanel createMainPanel(JFrame frame) {
        JPanel panel = new JPanel();
        frame.setLayout(layout);
        JTextField field = new JTextField("Server");
        JButton button = new JButton("Connect");
        addComponentGBag(frame, button, 0, 0, 2, 1);
        addComponentGBag(frame, field, 0, 1, 2, 1);
        return panel;
    }
    public void createFrame(Client client) {
        layout = new GridBagLayout();
//        File file = ((IServer) connection.getServer()).getServerFileSystem(connection).getFile("C:\\Users\\dolph\\Documents\\JmFiles\\PlumJuice\\build\\libs\\hello.txt");
//        System.out.println("Received file");
//        System.out.println(new LocalFile(file).readAllContents());
        JFrame frame = new JFrame("Done!");
//        frame.add(createMainPanel(frame));
        frame.setLayout(layout);
        JTextField field = new JTextField(40);
        JButton button = new JButton("Connect");
        addComponentGBag(frame, field, 0, 0, 2, 1);
        addComponentGBag(frame, button, 0, 1, 2, 1);
        frame.setSize(192 * 5, 108 * 5);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public Connection connect(Client client, int port) throws IOException {
        return client.connect(new ServerAddress(Machine.localIpAddress, port));
    }

    public Connection connect(Client client, InetAddress ip, int port) throws IOException {
        return client.connect(ip, port);
    }
}
