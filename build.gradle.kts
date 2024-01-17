plugins {
    id("java")
    id("application")
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.31"
}

group = "ca.pragmaticcoding"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = "5.10.0"



tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule = "ca.pragmaticcoding.pegsolitaire"
    mainClass = "ca.pragmaticcoding.pegsolitaire.PegSolitaire"
}
kotlin {
    jvmToolchain(17)
}

javafx {
    version = "20.0.1"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}

//test {
//    useJUnitPlatform()
//}

jlink {
    imageZip = project.file("${layout.buildDirectory}/distributions/app-${javafx.platform.classifier}.zip")
    options = listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages")
    launcher {
        name = "app"
    }
}

//jlinkZip {
//    group = "distribution"
//}