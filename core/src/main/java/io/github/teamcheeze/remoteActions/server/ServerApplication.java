package io.github.teamcheeze.remoteActions.server;

import io.github.teamcheeze.remoteActions.Initializer;
import io.github.teamcheeze.remoteActions.RemoteActions;
import java.io.IOException;

public class ServerApplication {
    public static void main(String[] args) {
        Initializer.initialize();
        Server server = RemoteActions.hostServer(10101);
        System.out.println(server.getAddress().ip.getHostAddress());
    }
}
