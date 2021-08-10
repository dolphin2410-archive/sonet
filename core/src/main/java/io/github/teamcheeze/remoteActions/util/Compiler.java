package io.github.teamcheeze.remoteActions.util;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class Compiler {
    public static void hi(File source) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();
        try (StandardJavaFileManager manager = compiler.getStandardFileManager(collector, null, null)) {
            Iterable<? extends JavaFileObject> sources = manager.getJavaFileObjectsFromFiles(Collections.singletonList(source));
            JavaCompiler.CompilationTask task = compiler.getTask(null, manager, collector, null, null, sources);
            task.call();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        hi(new File(""));
    }
}
