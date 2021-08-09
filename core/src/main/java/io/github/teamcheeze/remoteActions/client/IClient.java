package io.github.teamcheeze.remoteActions.client;

import io.github.teamcheeze.remoteActions.network.Address;
import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.network.IConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.Inet4Address;
import java.util.UUID;

public class IClient implements Client {
    private final UUID id;
    private Address address;

    public IClient() {
        this.id = UUID.randomUUID();
    }

    public IClient(Address address) {
        this.id = UUID.randomUUID();
        this.address = address;
    }

    @Override
    public @NotNull Connection connect(@NotNull Inet4Address ipv4, int port) {
        return new IConnection();
    }

    @Override
    public @NotNull UUID getId() {
        return id;
    }

    @NotNull
    @Override
    public Address getAddress() {
        return address;
    }
}
