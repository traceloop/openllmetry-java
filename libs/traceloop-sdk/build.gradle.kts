plugins {
    id("java")
}

group = "com.traceloop"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("io.opentelemetry:opentelemetry-bom:1.34.1"))
    implementation("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")
    implementation("io.opentelemetry:opentelemetry-exporter-logging")
    implementation("io.opentelemetry.semconv:opentelemetry-semconv:1.23.1-alpha");
    implementation("com.fasterxml.jackson.core:jackson-core:2.16.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.1")
    implementation("com.hubspot.jinjava:jinjava:2.7.1")
    implementation("com.theokanning.openai-gpt3-java:service:0.18.2")
    implementation("io.github.resilience4j:resilience4j-retry:2.1.0")

    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
