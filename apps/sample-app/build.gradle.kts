plugins {
    kotlin("jvm") version "1.9.22"
    application
}

group = "com.traceloop"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation(project(":libs:traceloop-sdk"))
    implementation("com.theokanning.openai-gpt3-java:service:0.18.2")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(19)
}

application {
    mainClass.set("MainKt")
}
