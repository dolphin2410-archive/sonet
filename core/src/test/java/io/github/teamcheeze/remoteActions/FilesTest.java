package io.github.teamcheeze.remoteActions;

import java.io.File;

public class FilesTest {
    public static void main(String[] args) {
        System.out.println(new File("/users").getAbsoluteFile().getName());
    }
}
