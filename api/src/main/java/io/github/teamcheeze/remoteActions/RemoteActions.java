package io.github.teamcheeze.remoteActions;

import io.github.teamcheeze.remoteActions.client.Client;
import io.github.teamcheeze.remoteActions.server.Server;

public class RemoteActions {
    private static RemoteActionsSupplier supplier;

    public static void setSupplier(RemoteActionsSupplier supplier) {
        if (RemoteActions.supplier != null) {
            throw new RuntimeException("You cannot re-initialize a supplier");
        }
        RemoteActions.supplier = supplier;
    }

    public static RemoteActionsSupplier getSupplier() {
        return supplier;
    }

    public static Client registerClient() {
        return getSupplier().registerClient();
    }

    public static Server hostServer(int port) {
        return getSupplier().hostServer(port);
    }

}
