package io.github.teamcheeze.remoteActions.server;

import io.github.teamcheeze.remoteActions.network.Address;
import org.jetbrains.annotations.NotNull;

import java.net.Inet4Address;
import java.util.UUID;

public interface Server {
    @NotNull
    UUID getId();

    @NotNull Address getAddress();

    void initialize();
}
