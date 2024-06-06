plugins {
    kotlin("jvm") version "1.9.23"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.telegram:telegrambots:6.0.1")
    implementation("org.telegram:telegrambotsextensions:6.0.1")
    implementation("org.slf4j:slf4j-simple:1.7.32")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
}

tasks.test {
    useJUnitPlatform()
}