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
- If no applicable price exists: HTTP 404.

#### Parameters

| Name             | Type     | Description                                              |
|------------------|----------|----------------------------------------------------------|
| `applicationDate`| `String` | Application date (ISO-8601 format, e.g., `2020-06-14T10:00:00`) |
| `productId`      | `Integer`| Product identifier                                       |
| `brandId`        | `Integer`| Brand identifier                                         |

#### Example of a successful response

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