package io.github.teamcheeze.remoteActions.network;

import org.jetbrains.annotations.NotNull;

public interface Connection {
    @NotNull
    Address getAddress();
}
