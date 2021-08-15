package io.github.teamcheeze.remoteActions.sample;

import io.github.teamcheeze.remoteActions.Initializer;
import io.github.teamcheeze.remoteActions.RemoteActions;
import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.data.sample.SampleOne;
import io.github.teamcheeze.remoteActions.network.data.sample.SampleTwo;
import java.io.IOException;

public class ClientApplication {
    public static void main(String[] args) throws IOException {
        Initializer.initialize();
        Client client = RemoteActions.registerClient();
        Connection connection = client.connect(44444);
        connection.sendPacket(new SampleOne());
        connection.sendPacket(new SampleTwo());
        connection.abort();
    }
}