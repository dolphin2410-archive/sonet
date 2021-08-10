package io.github.teamcheeze.remoteActions.network.data.fs;

import io.github.teamcheeze.remoteActions.network.PacketData;
import io.github.teamcheeze.remoteActions.server.IServer;
import io.github.teamcheeze.remoteActions.server.Server;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class FileChangeRequest implements PacketData {
    private FileChangeAction actionType;
    private Consumer<File> action;
    public FileChangeRequest() {

    }
    public FileChangeRequest(FileChangeAction actionType, Consumer<File> action) {
        this.actionType = actionType;
        this.action = action;
    }

    public FileChangeAction getActionType() {
        return actionType;
    }

    public void acceptRequest(Server server) throws IOException {
        action.accept(((IServer)server).config.getRootFile());
    }
}
