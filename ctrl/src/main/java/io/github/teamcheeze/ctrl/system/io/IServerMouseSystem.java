package io.github.teamcheeze.ctrl.system.io;

import io.github.teamcheeze.ctrl.system.features.ServerMouseSystem;
import io.github.teamcheeze.remoteActions.network.Connection;

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
