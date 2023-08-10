plugins {
    kotlin("jvm") version "1.6.21"
    java
}

group = "de.flerbuster"
version = "0.1.8.7"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("dev.kord:kord-core:0.8.0-M12")
}