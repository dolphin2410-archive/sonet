package io.github.teamcheeze.remoteActions.network;

import java.io.Serializable;

public interface NetworkComponent extends Serializable {
    default void validate() {
        if (!isValid()) {
            throw new RuntimeException("Trying to access an invalid network component. [Invalid server / client]");
        }
    };

    boolean isValid();

    void setValid(boolean valid);
}
