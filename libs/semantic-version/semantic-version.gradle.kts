plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("jvm")
    idea
}

dependencies {
    api(libs.kotlinLogging)
    api(libs.kandy)

    testImplementation(libs.bundles.kotest)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    jvmToolchain(17)
}

java {
    withSourcesJar()
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
