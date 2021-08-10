plugins {
    java
}

group = "teamcheeze"
version = "0.0.1-Beta"

allprojects {
    apply(plugin = "java")
    configurations {
        create("shade")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    configurations["shade"](project(":api"))
    configurations["shade"](project(":core"))
    implementation(project(":api"))
    implementation(project(":core"))
}

tasks {
    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        allprojects.forEach { project ->
            from (project.configurations["shade"].map { if (it.isDirectory) it else zipTree(it) })
        }
    }
    create<Jar>("buildClient") {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest {
            attributes["Main-Class"] = "io.github.teamcheeze.remoteActions.client.ClientApplication"
        }
        allprojects.forEach { project ->
            from (project.configurations["shade"].map { if (it.isDirectory) it else zipTree(it) })
        }
    }
    create<Jar>("buildServer") {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest {
            attributes["Main-Class"] = "io.github.teamcheeze.remoteActions.server.ServerApplication"
        }
        allprojects.forEach { project ->
            from (project.configurations["shade"].map { if (it.isDirectory) it else zipTree(it) })
        }
    }
}