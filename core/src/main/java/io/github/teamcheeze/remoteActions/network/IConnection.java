package io.github.teamcheeze.remoteActions.network;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class IConnection implements Connection {
    private Address address;
    public IConnection() {

    }
    public IConnection(@NotNull Address address, @NotNull UUID clientId, @NotNull UUID hostId) {
        this.address = address;
    }
    @Override
    @NotNull
    public Address getAddress() {
        return address;
    }
}
