plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"

    java
}


group = "de.flerbuster"
version = "1.8.7"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("dev.kord:kord-core:0.15.0")
}