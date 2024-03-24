plugins {
    kotlin("jvm") version "1.9.22"
    id("org.jetbrains.compose") version "1.5.12"
    `maven-publish`
}

group = "com.github.numq"
version = "1.0.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(project(":library"))
}

kotlin {
    jvmToolchain(11)
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            artifactId = "example"
        }
    }
}

tasks.withType<Wrapper> {
    gradleVersion = "7.4.2"
    distributionType = Wrapper.DistributionType.ALL
}