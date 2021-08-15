package io.github.teamcheeze.ctrl;

import io.github.teamcheeze.remoteActions.Initializer;
import io.github.teamcheeze.remoteActions.RemoteActions;
import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.network.Connection;

import java.io.IOException;

public class CtrlClient {
    public static void main(String[] args) throws IOException {
        Initializer.initialize();
        Client client = RemoteActions.registerClient();
        Connection connection = client.connect(44444);
        connection.abort();
    }
}
