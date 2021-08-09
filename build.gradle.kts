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
}

tasks {
    jar {
        allprojects.forEach { project ->
            from (project.configurations["shade"].map { if (it.isDirectory) it else zipTree(it) })
        }
    }
}