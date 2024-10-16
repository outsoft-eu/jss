plugins {
    kotlin("jvm") version "2.0.20"
    `java-library`
    `maven-publish`
}

group = "com.cashir"
version = "1.2-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.data:spring-data-jpa:2.4.3")
    implementation("jakarta.persistence:jakarta.persistence-api:2.2.3")
    testImplementation(kotlin("test"))
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
    testImplementation("io.mockk:mockk:1.13.13")
    testImplementation("org.mockito:mockito-inline:2.8.47")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "local"
            url = uri("file://${System.getProperty("user.home")}/.m2/repository")
        }
    }
}