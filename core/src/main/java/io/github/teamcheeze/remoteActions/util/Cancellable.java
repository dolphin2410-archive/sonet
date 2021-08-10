package io.github.teamcheeze.remoteActions.util;

public interface Cancellable {
    boolean isCanceled();
    void setCanceled(boolean canceled);
}
