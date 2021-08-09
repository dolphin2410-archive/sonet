package io.github.teamcheeze.remoteActions.server;

import io.github.teamcheeze.remoteActions.network.Address;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class IServer implements Server {
    private final UUID id;
    private Address address;
    public IServer() {
        this.id = UUID.randomUUID();
    }
    public IServer(Address address) {
        this.id = UUID.randomUUID();
        this.address = address;
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

    @Override
    public void initialize() {

    }
}
