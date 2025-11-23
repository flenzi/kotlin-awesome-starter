# Spring Boot 4.0 Upgrade Guide

## Current Status

Spring Boot 4.0.0 was officially released on **November 20, 2025**. While the artifacts are available in Maven Central, there are currently some challenges with the Gradle plugin resolution in certain environments.

## What's New in Spring Boot 4.0

### Requirements
- **Java 17+** (Java 21 recommended - already configured in this project ✓)
- **Jakarta EE 11** baseline (Servlet 6.1+)
- **Spring Framework 7.0**

### Major Dependency Updates
- Spring Framework 7.0.1
- Spring Data 2025.1.0
- Spring Security 7.0
- Spring Batch 6.0
- Spring Integration 7.0
- Hibernate 7.1
- Micrometer 1.16
- Reactor 2025.0

### Third-Party Updates
- H2 2.4
- HikariCP 7.0
- Flyway 11.11
- Elasticsearch Client 9.1

## Required Changes

### 1. build.gradle.kts

```kotlin
plugins {
    id("org.springframework.boot") version "4.0.0"
    id("io.spring.dependency-management") version "1.1.7"
    // ... other plugins remain the same
}

dependencies {
    // ... existing dependencies

    // Update springdoc-openapi for Spring Boot 4 compatibility
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.0")
}
```

### 2. settings.gradle.kts

Add plugin management configuration to help resolve the Spring Boot plugin:

```kotlin
rootProject.name = "kotlin-awesome-starter"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "org.springframework.boot") {
                useModule("org.springframework.boot:spring-boot-gradle-plugin:${requested.version}")
            }
        }
    }
}
```

## Known Issues

### Plugin Resolution
In some environments (particularly those using HTTP proxies), Gradle may have difficulty resolving the Spring Boot 4.0.0 plugin from the Gradle Plugin Portal, even though the artifacts exist in Maven Central.

**Verification**: The Spring Boot 4.0.0 plugin artifacts ARE available:
- ✓ Maven Central: https://repo1.maven.org/maven2/org/springframework/boot/spring-boot-gradle-plugin/4.0.0/
- ✓ GitHub Release: https://github.com/spring-projects/spring-boot/releases/tag/v4.0.0

### Workaround Options

If you encounter plugin resolution issues, try:

1. **Clear Gradle cache**:
   ```bash
   rm -rf ~/.gradle/caches
   gradle clean build --refresh-dependencies
   ```

2. **Configure HTTP proxy** (if behind a proxy):
   Create `~/.gradle/gradle.properties`:
   ```properties
   systemProp.http.proxyHost=your-proxy-host
   systemProp.http.proxyPort=your-proxy-port
   systemProp.https.proxyHost=your-proxy-host
   systemProp.https.proxyPort=your-proxy-port
   ```

3. **Wait for full propagation**: As Spring Boot 4.0.0 was just released (Nov 20, 2025), allow a few days for complete synchronization across all repository mirrors.

## Migration Guide

For detailed migration instructions, refer to the official guides:
- [Spring Boot 4.0 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Release-Notes)
- [Spring Boot 4.0 Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide)

## Testing

After upgrading, ensure you:
1. Run all tests: `gradle test`
2. Check for deprecation warnings
3. Verify all dependencies resolve correctly
4. Test your application's core functionality

## Rollback

If you need to rollback to Spring Boot 3.4.0:

```kotlin
plugins {
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
}

dependencies {
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
}
```

## Resources

- [Spring Boot Releases](https://github.com/spring-projects/spring-boot/releases)
- [Spring Boot 4 & Spring Framework 7 – What's New | Baeldung](https://www.baeldung.com/spring-boot-4-spring-framework-7)
- [Dependency Versions](https://docs.spring.io/spring-boot/appendix/dependency-versions/index.html)
- [Gradle Plugin Portal](https://plugins.gradle.org/plugin/org.springframework.boot)
- [Maven Central Repository](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-gradle-plugin)

## Current Project Status

This project is currently configured with Spring Boot 3.4.0 due to plugin resolution challenges in the build environment. The configuration is ready for upgrade to 4.0.0 when the environment allows proper plugin resolution.
