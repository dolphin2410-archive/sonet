repositories {
    mavenCentral()
}
dependencies {
    implementation("org.jetbrains:annotations:20.1.0")
    compileOnly(project(":api"))
    compileOnly("io.github.teamcheeze:jaw:1.0.2")
    shade("io.github.teamcheeze:jaw:1.0.2")
}