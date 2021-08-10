package io.github.teamcheeze.remoteActions;

import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.server.Server;

public interface RemoteActionsSupplier {
    Server hostServer(int port);
    Client registerClient();
}
