plugins {
    java
    `maven-publish`
    signing
}

group = "io.github.teamcheeze"
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
    configurations["shade"]("org.jetbrains:annotations:20.1.0")
    configurations["shade"]("io.github.teamcheeze:jaw:1.0.2")
}

tasks {
    jar {
        subprojects.forEach { project ->
            from(project.sourceSets["main"].output)
            from(project.configurations["shade"].map { if (it.isDirectory) it else zipTree(it) })
        }
    }
    register<Jar>("sourcesJar") {
        archiveClassifier.set("sourcesJar")
        subprojects.forEach {
            from(it.sourceSets["main"].allSource)
        }
    }
    register<Jar>("javadocJar") {
        archiveClassifier.set("javadocJar")
        from(javadoc.get())
    }
}

publishing {
    publications {
        create<MavenPublication>("publication") {
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])
            artifact(tasks.jar.get())
            repositories {
                mavenLocal()
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