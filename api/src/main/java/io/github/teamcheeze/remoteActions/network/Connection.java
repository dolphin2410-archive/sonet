package io.github.teamcheeze.remoteActions.network;

import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.server.Server;
import org.jetbrains.annotations.NotNull;

public interface Connection {
    @NotNull Client getClient();
    @NotNull Server getServer();
}
