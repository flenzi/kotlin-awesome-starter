# Kotlin Awesome Starter

> A production-ready Spring Boot template using Kotlin 2.1.0 with domain-driven design, comprehensive testing, security scanning, and CI/CD automation.

[![CI Pipeline](https://github.com/flenzi/kotlin-awesome-starter/workflows/CI%20Pipeline/badge.svg)](https://github.com/flenzi/kotlin-awesome-starter/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.0-blue.svg?logo=kotlin)](https://kotlinlang.org)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.0-brightgreen.svg?logo=spring)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg?logo=openjdk)](https://openjdk.org/)
[![Code Coverage](https://img.shields.io/badge/coverage-80%25-brightgreen)](https://github.com/flenzi/kotlin-awesome-starter/actions)
[![GitHub last commit](https://img.shields.io/github/last-commit/flenzi/kotlin-awesome-starter)](https://github.com/flenzi/kotlin-awesome-starter/commits)
[![GitHub issues](https://img.shields.io/github/issues/flenzi/kotlin-awesome-starter)](https://github.com/flenzi/kotlin-awesome-starter/issues)
[![GitHub stars](https://img.shields.io/github/stars/flenzi/kotlin-awesome-starter)](https://github.com/flenzi/kotlin-awesome-starter/stargazers)
[![Dependabot](https://img.shields.io/badge/Dependabot-enabled-brightgreen.svg?logo=dependabot)](https://github.com/flenzi/kotlin-awesome-starter/network/updates)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/flenzi/kotlin-awesome-starter/pulls)
[![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://github.com/flenzi/kotlin-awesome-starter/graphs/commit-activity)

## Overview

This is an **opinionated** Spring Boot starter template designed to accelerate new Kotlin projects with enterprise-grade best practices built-in from day one. No more boilerplate setup - clone, customize, and start building your application immediately.

### What Makes This Awesome?

- **Modern Stack**: Kotlin 2.1.0 + Spring Boot 4.0.0 + Gradle 8.14 with Kotlin DSL
- **Kotlin-First**: Uses `kotlinx-serialization` for JSON, `kotlin-logging` for logging, UUID v7 for entity IDs
- **Domain-Driven Design**: Clean architecture with domains containing controllers, services, repositories, and models
- **Architecture Enforcement**: Konsist tests ensure your team follows the established patterns
- **Database Migrations**: Liquibase for version-controlled schema management
- **Security Built-In**: Trivy container scanning + Dependabot automated updates
- **Quality Gates**: 80% code coverage requirement with JaCoCo
- **Automated Updates**: Dependabot configured for dependencies, GitHub Actions, and Docker base images
- **Production-Ready**: Multi-stage Docker builds, health checks, non-root user, optimized JVM settings
- **Observable**: Spring Boot Actuator with metrics, health checks, and Prometheus support

## Quick Start

### Prerequisites

- JDK 21
- Gradle 8.14+ (included via wrapper)
- Docker (optional, for containerized development)

### Run Locally

```bash
# Clone the repository
git clone https://github.com/flenzi/kotlin-awesome-starter.git
cd kotlin-awesome-starter

# Run the application
./gradlew bootRun

# Access the application
open http://localhost:8080

# View API documentation
open http://localhost:8080/swagger-ui.html

# View H2 Database Console (dev profile)
open http://localhost:8080/h2-console
```

### Run with Docker

```bash
# Build the Docker image
docker build -t kotlin-awesome-starter .

# Run the container
docker run -p 8080:8080 kotlin-awesome-starter

# Access the application
open http://localhost:8080
```

## Project Structure

```
src/main/kotlin/com/example/company/
├── domain/                          # Domain-driven modules
│   ├── user/                        # User domain
│   │   ├── controller/              # REST controllers
│   │   ├── service/                 # Business logic
│   │   ├── repository/              # Data access
│   │   └── model/                   # Entities & DTOs
│   └── product/                     # Product domain
│       ├── controller/
│       ├── service/
│       ├── repository/
│       └── model/
├── common/                          # Shared components
│   ├── config/                      # Application configuration
│   ├── exception/                   # Global exception handling
│   └── util/                        # Utility functions
└── Application.kt                   # Main application class

src/test/kotlin/com/example/company/
├── domain/                          # Domain tests
│   ├── user/
│   └── product/
└── architecture/                    # Konsist architecture tests
    └── ArchitectureTest.kt
```

## Domain-Driven Design Principles

This template enforces domain-driven architecture through Konsist tests:

1. **Package Structure**: Each domain must contain `controller`, `service`, `repository`, and `model` packages
2. **Naming Conventions**:
   - Controllers end with `Controller`
   - Services end with `Service`
   - Repositories end with `Repository`
3. **Layer Dependencies**:
   - Controllers depend on Services and Models
   - Services depend on Repositories and Models
   - Repositories depend only on Models
   - Models have no dependencies
4. **No Circular Dependencies**: Domains cannot depend on each other

### Adding a New Domain

```bash
# Create the directory structure
mkdir -p src/main/kotlin/com/example/company/domain/order/{controller,service,repository,model}
mkdir -p src/test/kotlin/com/example/company/domain/order

# Follow the patterns from existing domains (user, product)
```

## Key Technologies

### Core
- **Kotlin** 2.1.0 - Modern, concise, safe
- **Spring Boot** 4.0.0 - Production-ready application framework (Spring Framework 7.0, Jakarta EE 11)
- **Gradle** 8.14 with Kotlin DSL - Build automation
- **Java** 21 LTS - Runtime platform

### Kotlin-Specific Libraries
- **kotlinx-serialization** - Kotlin-native JSON serialization
- **kotlin-reflect** - Reflection for Spring
- **kotlinx-coroutines** - Asynchronous programming
- **kotlin-logging** - Idiomatic Kotlin logging

### Data & Persistence
- **Spring Data JPA** - Database abstraction
- **Liquibase** - Database migration management
- **UUID v7** - Time-ordered UUIDs (RFC 9562) for entity IDs
- **H2 Database** - In-memory database for development
- **PostgreSQL** - Production database driver

### Testing
- **JUnit 5** - Test framework
- **MockK** - Kotlin-friendly mocking
- **SpringMockK** - Spring + MockK integration
- **Konsist** - Architecture testing

### Code Quality & Security
- **JaCoCo** - Code coverage (80% minimum)
- **Konsist** - Architecture testing and enforcement
- **Trivy** - Container vulnerability scanning
- **Dependabot** - Automated dependency updates

### Documentation
- **SpringDoc OpenAPI** - Automatic API documentation

## Configuration

### Application Profiles

The template includes four configuration profiles:

- **default** (`application.yml`) - Base configuration
- **dev** (`application-dev.yml`) - Development with H2 database, SQL logging
- **test** (`application-test.yml`) - Testing configuration
- **prod** (`application-prod.yml`) - Production with PostgreSQL

Switch profiles:
```bash
# Via command line
./gradlew bootRun --args='--spring.profiles.active=prod'

# Via environment variable
export SPRING_PROFILES_ACTIVE=prod
./gradlew bootRun
```

### Environment Variables (Production)

```bash
DATABASE_URL=jdbc:postgresql://localhost:5432/proddb
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=your-secure-password
```

## Testing

```bash
# Run all tests
./gradlew test

# Run architecture tests only
./gradlew test --tests "*ArchitectureTest"

# Run with coverage
./gradlew test jacocoTestReport

# Verify coverage thresholds
./gradlew jacocoTestCoverageVerification
```

## CI/CD Pipeline

The GitHub Actions pipeline includes:

1. **Validate** - Gradle wrapper verification
2. **Build** - Compile application
3. **Test** - Unit & integration tests
4. **Architecture Tests** - Konsist validation
5. **Code Coverage** - JaCoCo report + 80% threshold check
6. **Docker Build & Push** - Multi-stage optimized images with Trivy scanning
7. **Publish Reports** - Test results and coverage reports

Security scanning results are uploaded to GitHub Security tab and as workflow artifacts.

## Security Features

### Container Scanning
- **Trivy** scans Docker images for vulnerabilities
- Scans for OS packages and application dependencies
- Results uploaded to GitHub Security tab

### Automated Updates
- **Dependabot** creates weekly PRs for:
  - Gradle dependencies (grouped by category)
  - GitHub Actions
  - Docker base images

## Docker

### Production Image Features
- **Multi-stage build** for minimal image size
- **Non-root user** for security
- **Health checks** for container orchestration
- **Optimized JVM settings** for containers
- **Alpine-based** for minimal attack surface

### Building & Running

```bash
# Build
docker build -t kotlin-awesome-starter:latest .

# Run with environment variables
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DATABASE_URL=jdbc:postgresql://host:5432/db \
  -e DATABASE_USERNAME=user \
  -e DATABASE_PASSWORD=pass \
  kotlin-awesome-starter:latest

# Health check
curl http://localhost:8080/actuator/health
```

## API Documentation

The template includes SpringDoc OpenAPI for automatic API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### Example Endpoints

**Users**
- `POST /api/users` - Create user
- `GET /api/users` - List all users
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/active` - List active users
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

**Products**
- `POST /api/products` - Create product
- `GET /api/products` - List all products
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products/available` - List available products
- `GET /api/products/search?name=query` - Search products
- `PUT /api/products/{id}` - Update product
- `PATCH /api/products/{id}/stock` - Update stock
- `DELETE /api/products/{id}` - Delete product

## Customization Guide

### 1. Change Package Name

Find and replace `com.example.company` with your package name:

```bash
find src -type f -name "*.kt" -exec sed -i 's/com.example.company/com.yourcompany.yourapp/g' {} +
```

Then rename directories to match.

### 2. Rename Project

Update in:
- `settings.gradle.kts` - `rootProject.name`
- `gradle.properties` - `group`
- `README.md` - All references

### 3. Configure Database

Edit `src/main/resources/application-prod.yml` for your production database.

### 4. Add New Domain

Follow the existing domain structure (user/product) and the architecture tests will ensure compliance.

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Quality Requirements
- All tests must pass
- Code coverage ≥ 80%
- Architecture tests (Konsist) must pass
- No high/critical security vulnerabilities

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

- **Issues**: [GitHub Issues](https://github.com/flenzi/kotlin-awesome-starter/issues)
- **Discussions**: [GitHub Discussions](https://github.com/flenzi/kotlin-awesome-starter/discussions)

## Roadmap

- [x] Add Liquibase for database migrations
- [ ] Add Redis caching example
- [ ] Add Kafka/RabbitMQ messaging example
- [ ] Add GraphQL support
- [ ] Add rate limiting
- [ ] Add distributed tracing (OpenTelemetry)
- [ ] Kubernetes deployment manifests
- [ ] Terraform infrastructure as code

---

**Built with ❤️ using Kotlin and Spring Boot**