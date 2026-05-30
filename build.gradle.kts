import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.kotlin.serialization)

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
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.reactor)

    // Kotlinx Serialization for JSON handling
    implementation(libs.kotlinx.serialization.json)

    // Database & Migrations
    implementation(libs.liquibase.core)
    runtimeOnly(libs.h2)
    runtimeOnly(libs.postgresql)

    // API Documentation
    implementation(libs.springdoc.openapi.starter.webmvc.ui)

    // Logging
    implementation(libs.kotlin.logging.jvm)

    // Development Tools
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.springmockk)

    // Architecture Testing with Konsist
    testImplementation(libs.konsist)
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
    toolVersion = libs.versions.jacoco.get()
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
