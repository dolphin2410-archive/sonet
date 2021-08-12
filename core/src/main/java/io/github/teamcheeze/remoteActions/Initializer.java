package io.github.teamcheeze.remoteActions;

public class Initializer {
    public static void initialize() {
        RemoteActions.setSupplier(new IRemoteActionsSupplier());
    }
}
