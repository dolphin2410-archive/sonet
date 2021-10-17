repositories {
    mavenCentral()
}
dependencies {
    testImplementation(platform("org.junit:junit-bom:5.7.2"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    implementation("org.jetbrains:annotations:22.0.0")
    implementation(project(":api"))
    implementation("io.github.teamcheeze:jaw:1.0.4")
}

tasks {
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}