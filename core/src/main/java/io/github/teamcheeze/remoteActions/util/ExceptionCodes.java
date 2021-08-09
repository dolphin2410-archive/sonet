package io.github.teamcheeze.remoteActions.util;

public enum ExceptionCodes {
    DUPLICATE_CLIENT_CODE(-100);
    private final int id;
    ExceptionCodes(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
