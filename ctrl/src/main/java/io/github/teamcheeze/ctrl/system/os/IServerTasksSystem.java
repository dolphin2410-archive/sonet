package io.github.teamcheeze.ctrl.system.os;

import io.github.teamcheeze.ctrl.system.features.ServerTasksSystem;
import io.github.teamcheeze.remoteActions.network.Connection;

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
