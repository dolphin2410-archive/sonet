plugins {
    java
    `maven-publish`
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
        dependsOn(project.tasks.build)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveFileName.set("remoteActionsClient.jar")
        manifest {
            attributes["Main-Class"] = "io.github.teamcheeze.remoteActions.client.ClientApplication"
        }
        allprojects.forEach { project ->
            from (project.configurations["shade"].map { if (it.isDirectory) it else zipTree(it) })
        }
    }
    create<Jar>("buildServer") {
        dependsOn(project.tasks.build)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveFileName.set("remoteActionsServer.jar")
        manifest {
            attributes["Main-Class"] = "io.github.teamcheeze.remoteActions.server.ServerApplication"
        }
        allprojects.forEach { project ->
            from (project.configurations["shade"].map { if (it.isDirectory) it else zipTree(it) })
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("pub") {
            repositories {
                mavenLocal()
            }
            pom {
                name.set("")
                description.set("Command dsl for paper server")
                url.set("https://github.com/monun/kommand")

                licenses {
                    license {
                        name.set("GNU General Public License version 3")
                        url.set("https://opensource.org/licenses/GPL-3.0")
                    }
                }

                developers {
                    developer {
                        id.set("monun")
                        name.set("Monun")
                        email.set("monun1010@gmail.com")
                        url.set("https://github.com/monun")
                        roles.addAll("developer")
                        timezone.set("Asia/Seoul")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/monun/tap.git")
                    developerConnection.set("scm:git:ssh://github.com:monun/tap.git")
                    url.set("https://github.com/monun/tap")
                }
            }
        }
    }
}