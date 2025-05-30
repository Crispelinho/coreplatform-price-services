# Inditex Core Platform - Technical Test

## Description

This project is a backend application developed with **Spring Boot** using a **reactive approach (WebFlux)** and data access via **R2DBC**, simulating part of Inditex's core ecommerce system.

The application exposes REST endpoints to query the applicable price for a product at a given date, according to its brand and the priority rules in the `PRICES` table.

The database used is **in-memory H2**, automatically initialized with test data at application startup.

---

## Features

- REST endpoints for price queries.
- Use of DTOs to structure responses.
- Hexagonal architecture and SOLID principles.
- Configuration and dependencies managed with Gradle.
- Unit and integration tests.
- Test coverage and code quality analysis with Jacoco and SonarQube.
- CI/CD pipeline with example configuration for GitHub Actions.

## Technologies Used

- Java 17
- Spring Boot 3.4.5
- Spring WebFlux
- Spring Data R2DBC
- H2 Database (in-memory)
- JUnit 5 / Reactor Test
- Jacoco (test coverage)
- SonarQube (code quality analysis)
- Gradle

---

## Data Model

The `PRICES` table contains the following fields:

| Field        | Description                                                                |
|-------------|----------------------------------------------------------------------------|
| `BRAND_ID`  | Brand identifier (e.g., 1 = ZARA)                                          |
| `START_DATE`, `END_DATE` | Validity date range for the price                        |
| `PRICE_LIST`| Rate identifier                                                            |
| `PRODUCT_ID`| Product identifier                                                         |
| `PRIORITY`  | Priority level to resolve price overlaps                                   |
| `PRICE`     | Final applicable price                                                     |
| `CURR`      | Currency (ISO format, e.g., EUR)                                           |

---

## Installation

1. Clone the repository or download the source code.
2. Make sure you have Java 17 and Gradle installed.
3. From the project root, run:

```sh
./gradlew build
```

4. To start the application locally:

```sh
./gradlew bootRun
```

5. The service will be available at: `http://localhost:8080`

6. To run the tests and view the coverage report:

```sh
./gradlew test jacocoTestReport
```

- The Jacoco coverage report will be at `build/reports/jacoco/test/html/index.html`.
- The test report will be at `build/reports/tests/test/index.html`.

7. (Optional) To analyze code quality with SonarQube, make sure you have a SonarQube server available and configure the necessary properties in `build.gradle` or `sonar-project.properties`.

## SonarQube Integration

To analyze code quality with SonarQube, follow these steps:

1. Download SonarQube Developer Edition from:
   <https://www.sonarsource.com/sem/products/sonarqube/downloads/success-download-developer-edition/>

2. Start the SonarQube server locally (default at <http://localhost:9000>).

3. Set up your project in SonarQube by visiting:
   <http://localhost:9000/dashboard?id=price-service>

4. Run the following command to clean, test, generate the coverage report, and launch the SonarQube analysis:

```sh
./gradlew clean test jacocoTestReport sonar -Dsonar.token=<SONAR_TOKEN>
```

Replace `<SONAR_TOKEN>` with your personal SonarQube token, for example:

```sh
./gradlew clean test jacocoTestReport sonar "-Dsonar.token=sqp_11a00eb0eb9a2afdd66c9cb83c225bcd947793fa"
```

- The Jacoco coverage report will be at `build/reports/jacoco/test/html/index.html`.
- The test report will be at `build/reports/tests/test/index.html`.
- The analysis results will be available in the SonarQube dashboard.

---

## Available Endpoints

### 1. Get all prices

**GET /prices**

- Returns a list of all available prices in JSON format.
- Success response: HTTP 200 and an array of `PriceResponse` objects.
- If no prices exist: HTTP 404.

#### Example of a successful response

```json
[
  {
    "productId": 35455,
    "brandId": 1,
    "rateId": 2,
    "startDate": "2020-06-14T15:00:00",
    "endDate": "2020-06-14T18:30:00",
    "price": 25.45
  }
]
```

### 2. Get applicable price by product, brand, and date

**GET /applicationPrices?productId={productId}&brandId={brandId}&applicationDate={date}**

- Returns the applicable price for a given product, brand, and date.
- Success response: HTTP 200 and a `PriceResponse` object.
- If no applicable price exists: HTTP 404 (no body).
- If any required parameter is missing or invalid: HTTP 400 and an error body.
- If an internal server error occurs: HTTP 500 and an error body.

#### Parameters

| Name             | Type     | Description                                              |
|------------------|----------|----------------------------------------------------------|
| `applicationDate`| `String` | Application date (ISO-8601 format, e.g., `2020-06-14T10:00:00`) |
| `productId`      | `Integer`| Product identifier                                       |
| `brandId`        | `Integer`| Brand identifier                                         |

#### Example of a successful response (HTTP 200)

```json
{
  "productId": 35455,
  "brandId": 1,
  "rateId": 2,
  "startDate": "2020-06-14T15:00:00",
  "endDate": "2020-06-14T18:30:00",
  "price": 25.45
}
```

#### Example of a response when no applicable price exists

- HTTP status code: 404 Not Found

#### Example of a bad request response (HTTP 400)

When a parameter is invalid (e.g., negative brandId):

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "'getApplicablePrice.arg1' must be a positive integer",
  "path": "/api/prices/applicable",
  "timestamp": "2025-05-29T19:20:02.1970393"
}
```

When a required parameter is missing:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Required request parameter 'productId' for method parameter type Integer is not present",
  "path": "/api/prices/applicable",
  "timestamp": "2025-05-29T19:21:10.1234567"
}
```

#### Example of an internal server error response (HTTP 500)

```json
{
  "status": 500,
  "error": "Internal Server Error",
  "message": "Unexpected error occurred while processing the request.",
  "path": "/api/prices/applicable",
  "timestamp": "2025-05-29T19:22:30.9876543"
}
```

---

## Main Folder and File Structure

```
coreplatform-price-services/
├── .gitignore
├── build.gradle
├── CHANGELOG.md
├── gradlew
├── gradlew.bat
├── README.md
├── settings.gradle
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── inditex/
│   │   │           └── coreplatform/
│   │   │               └── price_service/
│   │   │                   ├── PriceServiceApplication.java
│   │   │                   ├── application/
│   │   │                   │   ├── exceptions/
│   │   │                   │   │   └── MissingPriceApplicationRequestParamException.java
│   │   │                   │   ├── service/
│   │   │                   │   │   └── ReactivePriceService.java
│   │   │                   │   └── usecases/
│   │   │                   │       ├── GetPricesUseCase.java
│   │   │                   │       └── queries/
│   │   │                   │           └── GetApplicablePriceQuery.java
│   │   │                   ├── domain/
│   │   │                   │   ├── models/
│   │   │                   │   │   └── Price.java
│   │   │                   │   └── ports/
│   │   │                   ├── infrastructure/
│   │   │                   │   ├── config/
│   │   │                   │   │   ├── UseCaseBeanConfig.java
│   │   │                   │   ├── mappers/
│   │   │                   │   │   ├── PriceMapper.java
│   │   │                   │   │   └── PriceMapperImpl.java
│   │   │                   │   ├── persistence/
│   │   │                   │   │   ├── entities/
│   │   │                   │   │   │   └── PriceEntity.java
│   │   │                   │   │   ├── repositories/
│   │   │                   │   │   │   ├── IReactivePriceRepository.java
│   │   │                   │   │   │   └── PriceRepositoryAdapter.java
│   │   │                   │   ├── rest/
│   │   │                   │       ├── controllers/
│   │   │                   │       │   ├── PriceController.java
│   │   │                   │       │   └── dtos/
│   │   │                   │       │       ├── ErrorPriceResponse.java
│   │   │                   │       │       └── PriceResponse.java
│   │   │                   │       └── exceptions/
│   │   │                   │           └── GlobalExceptionHandler.java
│   │   │                   └── PriceServiceApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── data.sql
│   │       └── schema.sql
│   └── test/
│       └── java/
│           └── com/
│               └── inditex/
│                   └── coreplatform/
│                       └── price_service/
│                           ├── PriceServiceApplicationTests.java
│                           ├── application/
│                           │   ├── exceptions/
│                           │   │   └── MissingPriceApplicationRequestParamExceptionTest.java
│                           │   ├── service/
│                           │   │   └── ReactivePriceServiceTest.java
│                           │   └── usecases/
│                           │       ├── GetApplicablePriceUseCaseTest.java
│                           │       ├── GetPricesUseCaseTest.java
│                           │       └── queries/
│                           │           └── GetApplicablePriceQueryTest.java
│                           ├── domain/
│                           │   └── models/
│                           │       └── PriceTest.java
│                           ├── infrastructure/
│                           │   ├── mappers/
│                           │   │   └── PriceMapperImplTest.java
│                           │   ├── persistence/
│                           │   │   └── repositories/
│                           │   │       └── PriceRepositoryAdapterTest.java
│                           │   └── rest/
│                           │       ├── controllers/
│                           │       │   └── PriceControllerTest.java
│                           │       └── exceptions/
│                           │           └── GlobalExceptionHandlerTest.java
build/
  ... (build and test generated files)
.github/
  workflows/
    ci.yml
    cd.yml
    backport.yml
load-tests/
  load-test.js

```

---

## Continuous Integration and Deployment (CI/CD)

The project includes automated pipelines configured in the `.github/workflows/` folder:

- **ci.yml**: Runs the continuous integration pipeline on every push, pull request, or relevant branch creation. Checks out the code, sets up JDK 17, runs build, tests, and generates coverage reports with Jacoco. Results and reports are uploaded as workflow artifacts.
- **cd.yml**: Continuous deployment pipeline, responsible for publishing the generated artifact to a test or production environment after passing tests and validations.
- **backport.yml**: Automates the creation of backport pull requests when merging to `main`, facilitating synchronization with `develop` or `release/*` branches.

---
