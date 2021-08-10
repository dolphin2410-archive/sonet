package io.github.teamcheeze.remoteActions.util;

import io.github.dolphin2410.jaw.util.collection.development.SimpleIterator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class Directory extends AbstractList<File> implements List<File> {
    private final File[] children;
    public Directory(String dirname) {
        this.children = new File(dirname).listFiles();
    }

    @Override
    public File get(int index) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}
