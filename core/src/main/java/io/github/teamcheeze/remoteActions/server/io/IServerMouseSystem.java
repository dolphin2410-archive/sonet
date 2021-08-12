package io.github.teamcheeze.remoteActions.server.io;

import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.server.features.ServerMouseSystem;

public class IServerMouseSystem implements ServerMouseSystem {
    private Connection connection;
    public IServerMouseSystem(Connection connection) {
        this.connection = connection;
    }
    @Override
    public Connection getConnection() {
        return connection;
    }
}
