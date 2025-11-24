import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "4.0.0"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.spring") version "2.1.0"
    kotlin("plugin.jpa") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"

    // Code Quality & Coverage
    id("jacoco")
}

group = "com.example.company"
version = "1.0.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Starters
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Kotlin Specific Libraries
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.10.2")

    // Kotlinx Serialization for JSON handling
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    // Database & Migrations
    implementation("org.liquibase:liquibase-core")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")

    // API Documentation
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.0")

    // Logging
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")

    // Development Tools
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    testImplementation("io.mockk:mockk:1.13.13")
    testImplementation("com.ninja-squad:springmockk:4.0.2")

    // Architecture Testing with Konsist
    testImplementation("com.lemonappdev:konsist:0.16.1")
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.set(listOf(
            "-Xjsr305=strict",           // Strict null-safety
            "-Xjvm-default=all",          // Generate default methods in interfaces
            "-opt-in=kotlin.RequiresOptIn" // Enable experimental APIs
        ))
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1)
        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

// JaCoCo Configuration for Code Coverage
jacoco {
    toolVersion = "0.8.12"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }

    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude(
                    "**/config/**",
                    "**/Application.kt",
                    "**/model/**",
                    "**/dto/**"
                )
            }
        })
    )
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.jacocoTestReport)

    violationRules {
        rule {
            limit {
                minimum = "0.80".toBigDecimal() // 80% minimum coverage
            }
        }

        rule {
            element = "CLASS"
            limit {
                minimum = "0.70".toBigDecimal()
            }
            excludes = listOf(
                "*.config.*",
                "*.Application",
                "*.model.*",
                "*.dto.*"
            )
        }
    }
}

// Custom task to check code coverage
tasks.register("checkCoverage") {
    dependsOn(tasks.jacocoTestCoverageVerification)
    description = "Runs tests and verifies code coverage meets minimum thresholds"
    group = "verification"
}

// Build task dependencies
tasks.build {
    dependsOn(tasks.jacocoTestReport)
}
