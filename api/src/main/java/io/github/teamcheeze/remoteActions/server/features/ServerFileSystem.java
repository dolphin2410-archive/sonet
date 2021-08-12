package io.github.teamcheeze.remoteActions.server.features;

import io.github.teamcheeze.remoteActions.util.RemoteFile;

import java.io.File;

public interface ServerFileSystem extends ServerSideSystem {
    RemoteFile getFile(String filename);
}
