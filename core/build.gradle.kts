repositories {
    mavenCentral()
}
dependencies {
    implementation("org.jetbrains:annotations:20.1.0")
    compileOnly(project(":api"))
}