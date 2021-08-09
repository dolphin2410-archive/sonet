package io.github.teamcheeze.remoteActions.client;

import io.github.teamcheeze.remoteActions.network.Address;
import io.github.teamcheeze.remoteActions.network.Connection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.Inet4Address;
import java.util.UUID;

public interface Client {
    @NotNull
    Connection connect(Inet4Address ipv4, int port);

    @NotNull
    UUID getId();

    @NotNull Address getAddress();
}
