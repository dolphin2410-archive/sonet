package io.github.teamcheeze.remoteActions.util;

import java.io.Serializable;

public abstract class Listener<T> implements Serializable {
    public abstract void onAction(T obj);
}
