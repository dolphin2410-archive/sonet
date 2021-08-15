package io.github.teamcheeze.ctrl.system.features;

import io.github.teamcheeze.remoteActions.util.RemoteFile;

public interface ServerFileSystem extends ServerSideSystem {
    RemoteFile getFile(String filename);
}
