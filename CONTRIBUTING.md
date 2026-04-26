# Contributing to Kotlin Awesome Starter

Thank you for your interest in contributing to Kotlin Awesome Starter! This document provides guidelines and instructions for contributing.

## Code of Conduct

- Be respectful and inclusive
- Welcome newcomers and help them learn
- Focus on what is best for the community
- Show empathy towards other community members

## How to Contribute

### Reporting Bugs

Before creating bug reports, please check existing issues to avoid duplicates. When creating a bug report, include:

- A clear and descriptive title
- Steps to reproduce the issue
- Expected behavior
- Actual behavior
- Environment details (OS, Java version, etc.)
- Relevant logs or screenshots

### Suggesting Enhancements

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion, include:

- A clear and descriptive title
- A detailed description of the proposed functionality
- Explain why this enhancement would be useful
- List any similar features in other projects (if applicable)

### Pull Requests

1. **Fork the repository** and create your branch from `main`

2. **Follow the existing code style**
   - Use Kotlin idioms and conventions
   - Follow the domain-driven design structure
   - Maintain the established naming conventions

3. **Write tests**
   - Add unit tests for new functionality
   - Ensure all existing tests pass
   - Maintain or improve code coverage (≥ 80%)

4. **Run quality checks**
   ```bash
   # Run tests
   ./gradlew test

   # Check code coverage
   ./gradlew jacocoTestCoverageVerification

   # Run architecture tests
   ./gradlew test --tests "*ArchitectureTest"
   ```

5. **Update documentation**
   - Update README.md if you change functionality
   - Add KDoc comments for public APIs
   - Update OpenAPI documentation if you modify endpoints

6. **Commit your changes**
   - Use clear and meaningful commit messages
   - Follow conventional commits format:
     ```
     feat: add new feature
     fix: fix bug
     docs: update documentation
     test: add tests
     refactor: refactor code
     chore: update dependencies
     ```

7. **Submit a pull request**
   - Provide a clear description of the changes
   - Reference any related issues
   - Ensure CI pipeline passes

## Development Setup

### Prerequisites
- JDK 21
- Docker (for running PostgreSQL)

### Local Development

```bash
# Clone your fork
git clone https://github.com/YOUR-USERNAME/kotlin-awesome-starter.git
cd kotlin-awesome-starter

# Create a new branch
git checkout -b feature/my-feature

# Start PostgreSQL (optional)
docker-compose up -d postgres

# Run the application
./gradlew bootRun

# Run tests
./gradlew test
```

### Architecture Guidelines

This project follows domain-driven design principles:

1. **Each domain must contain**:
   - `controller/` - REST controllers
   - `service/` - Business logic
   - `repository/` - Data access
   - `model/` - Entities and DTOs

2. **Naming conventions**:
   - Controllers end with `Controller`
   - Services end with `Service`
   - Repositories end with `Repository`

3. **Layer dependencies**:
   - Controllers → Services → Repositories → Models
   - No circular dependencies between domains

4. **Testing requirements**:
   - Unit tests for services
   - Architecture tests with Konsist
   - Integration tests for controllers
   - Minimum 80% code coverage

### Code Style

- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use 4 spaces for indentation
- Maximum line length: 120 characters
- Use meaningful variable and function names
- Prefer immutability (`val` over `var`)
- Use data classes for DTOs and models
- Leverage Kotlin's null safety

### Adding a New Domain

```bash
# Create domain structure
mkdir -p src/main/kotlin/com/example/company/domain/newdomain/{controller,service,repository,model}
mkdir -p src/test/kotlin/com/example/company/domain/newdomain

# Follow existing patterns
# - Create entity in model/
# - Create repository interface
# - Create service with business logic
# - Create controller with REST endpoints
# - Write comprehensive tests

# Verify architecture compliance
./gradlew test --tests "*ArchitectureTest"
```

## Review Process

1. **Automated Checks**
   - All CI pipeline stages must pass
   - Code coverage must be ≥ 80%
   - Architecture tests must pass

2. **Manual Review**
   - Code follows project conventions
   - Changes are well-documented
   - Tests are comprehensive
   - No breaking changes (or properly documented)

3. **Merge**
   - Approved by at least one maintainer
   - All conversations resolved
   - CI pipeline green

## Questions?

- Open an issue for questions about contributing
- Use GitHub Discussions for general questions
- Tag maintainers for urgent matters

## License

By contributing, you agree that your contributions will be licensed under the MIT License.

---

Thank you for making Kotlin Awesome Starter better!
