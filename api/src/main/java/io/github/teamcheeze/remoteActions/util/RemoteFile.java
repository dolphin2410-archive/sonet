package io.github.teamcheeze.remoteActions.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.rmi.RemoteException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class RemoteFile {
    ByteArrayInputStream inputStream;
    String filename;

    public RemoteFile() {

    }

    public RemoteFile(ByteArrayInputStream outputStream, String filename) {
        this.inputStream = outputStream;
        this.filename = filename;
    }

    public RemoteFile(File file) {
        if (file.isDirectory()) {
            try {
                // Source: https://knpcode.com/java-programs/how-to-zip-folder-in-java/
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
                Path sourceDirectory = file.toPath();
                Files.walkFileTree(sourceDirectory, new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        if (sourceDirectory != dir) {
                            zipOutputStream.putNextEntry(new ZipEntry(sourceDirectory.relativize(dir) + "/"));
                            zipOutputStream.closeEntry();
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        zipOutputStream.putNextEntry(new ZipEntry(sourceDirectory.relativize(file).toString()));
                        Files.copy(file, zipOutputStream);
                        zipOutputStream.closeEntry();
                        return FileVisitResult.CONTINUE;
                    }
                });
                // Executes after looping all the files
                // Result = byteArrayOutputStream
                this.inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                this.filename = file.getName() + ".zip";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                this.inputStream = new ByteArrayInputStream(Files.readAllBytes(file.toPath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.filename = file.getName();
        }
    }
    public File downloadLocally(File sourceFolder) throws IOException {
        if (!sourceFolder.isDirectory()) {
            throw new RuntimeException("Not a folder");
        }
        File file = new File(sourceFolder, filename);
        Files.copy(inputStream, file.toPath());
        return file;
    }
    public List<String> readAllContents() {
        return new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        ).lines().toList();
    }

    public ByteArrayInputStream getInputStream() {
        return inputStream;
    }
}
