package io.github.teamcheeze.remoteActions.network;

import java.io.Serializable;

public interface NetworkComponent extends Serializable {
    void validate();

    boolean isValid();

    void setValid(boolean valid);
}
