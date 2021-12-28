plugins {
    java
    `maven-publish`
    signing
}

group = "io.github.teamcheeze"
version = "0.0.4"

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
    configurations["shade"]("org.jetbrains:annotations:22.0.0")
    configurations["shade"]("io.github.teamcheeze:jaw:1.0.4")
}

tasks {
    jar {
        subprojects.forEach { project ->
            from(project.sourceSets["main"].output)
            from(project.configurations["shade"].map { if (it.isDirectory) it else zipTree(it) })
        }
    }
    register<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
        subprojects.forEach {
            from(it.sourceSets["main"].allSource)
        }
    }
    register<Javadoc>("allJavadoc") {
        source(subprojects.map { it.sourceSets["main"].allSource })
        classpath = files(subprojects.map { it.sourceSets["main"].compileClasspath })
    }
    register<Jar>("javadocJar") {
        archiveClassifier.set("javadoc")
        val task = project.tasks["allJavadoc"]
        dependsOn(task)
        from((task as Javadoc).destinationDir)
    }
}

publishing {
    publications {
        create<MavenPublication>("publication") {
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])
            repositories {
                mavenLocal()
                maven {
                    name = "sonatype"
                    credentials.runCatching {
                        val nexusUsername: String by project
                        val nexusPassword: String by project
                        username = nexusUsername
                        password = nexusPassword
                    }.onFailure {
                        logger.warn("Failed to load nexus credentials, Check the gradle.properties")
                    }
                    url = uri(
                        if (version.endsWith("-SNAPSHOT") || version.endsWith(".Beta") || version.endsWith(".beta")) {
                            "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                        } else {
                            "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                        }
                    )
                }
            }
            pom {
                name.set("sonet")
                description.set("Simple, lightweight network manager.")
                url.set("https://github.com/dolphin2410/sonet")

                licenses {
                    license {
                        name.set("GNU General Public License version 3")
                        url.set("https://opensource.org/licenses/GPL-3.0")
                    }
                }

                developers {
                    developer {
                        name.set("dolphin2410")
                        email.set("dolphin2410@outlook.com")
                        url.set("https://github.com/dolphin2410")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/dolphin2410/sonet.git")
                    developerConnection.set("scm:git:ssh://github.com:dolphin2410/sonet.git")
                    url.set("https://github.com/dolphin2410/sonet")
                }
            }
        }
    }
}

signing {
    isRequired = true
    sign(tasks["javadocJar"],tasks["sourcesJar"], tasks.jar.get())
    sign(publishing.publications["publication"])
}

dependencies {
    configurations["shade"].forEach {
        implementation(files(it))
    }
}