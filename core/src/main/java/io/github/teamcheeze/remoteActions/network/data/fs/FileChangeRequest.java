package io.github.teamcheeze.remoteActions.network.data.fs;

import io.github.teamcheeze.remoteActions.server.IServer;
import io.github.teamcheeze.remoteActions.server.Server;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class FileChangeRequest {
    private FileChangeAction actionType;
    private Consumer<File> action;
    public FileChangeRequest() {

    }
    public FileChangeRequest(FileChangeAction actionType, Consumer<File> action) {
        this.actionType = actionType;
        this.action = action;
    }

    public void acceptRequest(Server server) throws IOException {
        action.accept(((IServer)server).config.getRootFile());
    }
}
