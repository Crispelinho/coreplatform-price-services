# Changelog

All significant changes to this project will be documented in this file.

## [1.0.1] - 2025-05-29

### Added

- Automatic backport action for merges into `main`, creating PRs to `develop` or `release/**` branches.
- CI/CD configuration for continuous integration and deployment.
- Automatic execution of tests and quality analysis on each push (build, test, Jacoco, SonarQube).
- Artifact generation and coverage report in the pipeline.

## [1.0.0] - 2025-05-29

### Added

- Initial project structure following hexagonal architecture.
- Implementation of REST endpoints for price queries.
- Domain model and DTOs (`Price`, `PriceResponse`).
- Services and use cases for business logic.
- Unit and integration tests with JUnit 5 and Reactor Test.
- Test coverage configuration with Jacoco.
- Quality analysis integration with SonarQube.
- In-memory H2 database with initialization scripts.
- Initial documentation in README.md.