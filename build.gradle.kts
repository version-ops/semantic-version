plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
}

allprojects {
    group = "io.github.waverunner.versions"
    version = "0.1.0-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
    }
}