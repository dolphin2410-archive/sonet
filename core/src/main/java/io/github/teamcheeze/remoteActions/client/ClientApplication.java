package io.github.teamcheeze.remoteActions.client;

import io.github.teamcheeze.remoteActions.Initializer;
import io.github.teamcheeze.remoteActions.RemoteActions;
import io.github.teamcheeze.remoteActions.network.server.ServerAddress;
import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.util.Machine;
import io.github.teamcheeze.remoteActions.util.RemoteFile;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.InetAddress;

public class ClientApplication {
    private static GridBagLayout layout;
    private Connection connection;
    private JFrame frame;
    private JButton connectButton;
    private JTextField ipField;

    public static void main(String[] args) {
//        connect(10101);
        Initializer.initialize();
        Client client = RemoteActions.registerClient();
        new ClientApplication().createFrame(client);
    }

    public boolean isConnected() {
        return connection != null;
    }

    // Source: https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=javaking75&logNo=140189054193
    public void addComponentGBag(Frame parent, Component c, int x, int y, int w, int h) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        layout.setConstraints(c, gbc);
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
//        System.out.println("Received file");
//        System.out.println(new LocalFile(file).readAllContents());
        JFrame frame = new JFrame("Done!");
        this.frame = frame;
//        frame.add(createMainPanel(frame));
        frame.setLayout(layout);
        JTextField field = new JTextField(40);
        JButton button = new JButton("Connect");
        this.ipField = field;
        this.connectButton = button;
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                String input = field.getText();
                if (input.matches("\\d{1,3}(\\.\\d{1,3}){3}:\\d{0,5}")) {
                    String[] split = input.split(":");
                    String ip = split[0];
                    int port = Integer.parseInt(split[1]);
                    try {
                        //InetAddress.getByName(ip)
                        System.out.println(ip);
                        System.out.println(port);
                        connection = connect(client, port);
                        JOptionPane.showMessageDialog(frame, "Connected!!");
                        System.out.println("Connected");
                    } catch (IOException ie) {
                        ie.printStackTrace();
                    }
                    refreshFrame();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        addComponentGBag(frame, field, 0, 0, 2, 1);
        addComponentGBag(frame, button, 0, 1, 2, 1);
        frame.setSize(192 * 5, 108 * 5);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void refreshFrame() {
        if (isConnected()) {
            System.out.println("Successfully connected to server");
            frame.remove(connectButton);
            frame.remove(ipField);
            frame.invalidate();
            frame.validate();
            frame.repaint();
            RemoteFile file = connection.fs().getFile("C:\\Users\\dolph\\Documents\\JmFiles\\RemoteActions\\build\\libs\\test.txt");
            for (String content : file.readAllContents()) {
                System.out.println(content);
            }
        } else {
            // You have to re add the button and field

        }
    }

    public Connection connect(Client client, int port) throws IOException {
        return client.connect(new ServerAddress(Machine.localIpAddress, port));
    }

    public Connection connect(Client client, InetAddress ip, int port) throws IOException {
        return client.connect(ip, port);
    }
}
