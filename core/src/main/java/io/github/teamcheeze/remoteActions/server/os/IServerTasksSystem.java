package io.github.teamcheeze.remoteActions.server.os;

import io.github.teamcheeze.remoteActions.network.Connection;
import io.github.teamcheeze.remoteActions.server.features.ServerSideSystem;
import io.github.teamcheeze.remoteActions.server.features.ServerTasksSystem;

public class IServerTasksSystem implements ServerTasksSystem {
    private Connection connection;
    public IServerTasksSystem(Connection connection) {
        this.connection = connection;
    }
    @Override
    public Connection getConnection() {
        return connection;
    }
}
