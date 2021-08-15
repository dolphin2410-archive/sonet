package io.github.teamcheeze.remoteActions.server;

import io.github.teamcheeze.remoteActions.network.NetworkComponent;
import io.github.teamcheeze.remoteActions.network.server.ServerAddress;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface Server extends NetworkComponent {
    @NotNull
    UUID getId();

    @NotNull ServerAddress getAddress();

    void initialize();
}
