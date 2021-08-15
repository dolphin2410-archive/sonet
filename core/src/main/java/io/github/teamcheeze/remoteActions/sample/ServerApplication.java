package io.github.teamcheeze.remoteActions.sample;

import io.github.teamcheeze.remoteActions.Initializer;
import io.github.teamcheeze.remoteActions.RemoteActions;
import io.github.teamcheeze.remoteActions.server.Server;

public class ServerApplication {
    public static void main(String[] args) {
        Initializer.initialize();
        Server server = RemoteActions.hostServer(44444);
        System.out.println(server.getAddress().ip.getHostAddress());
    }
}