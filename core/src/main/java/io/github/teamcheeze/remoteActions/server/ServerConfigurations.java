package io.github.teamcheeze.remoteActions.server;

import java.io.File;

public class ServerConfigurations {
    private File rootFile;

    public ServerConfigurations() {

    }

    public ServerConfigurations(File rootFile) {
        this.setRootFile(rootFile);
    }

    public File getRootFile() {
        return rootFile;
    }

    public void setRootFile(File rootFile) {
        this.rootFile = rootFile;
    }
}
