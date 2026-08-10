# bookstoreAPITesting

Serenity BDD and REST Assured API automation project for testing the Books and Authors REST API from [FakeRESTAPI](https://fakerestapi.azurewebsites.net/index.html).

## Overview

This project tests the following API resources:

- Books API
- Authors API

The Books API coverage includes:

- `GET /api/v1/Books`
- `GET /api/v1/Books/{id}`
- `POST /api/v1/Books`
- `PUT /api/v1/Books/{id}`
- `DELETE /api/v1/Books/{id}`

The Authors API is included as an additional extension:

- `GET /api/v1/Authors`
- `GET /api/v1/Authors/{id}`
- `POST /api/v1/Authors`
- `PUT /api/v1/Authors/{id}`
- `DELETE /api/v1/Authors/{id}`

## Technologies

- Java 17
- Maven
- JUnit 5
- Serenity BDD
- Serenity REST Assured
- REST Assured
- AssertJ
- Lombok
- Jackson
- GitHub Actions

## Project structure

```text
bookstoreAPITesting/
├── .github/
│   └── workflows/
│       └── api-tests.yml
├── pom.xml
└── src/
    └── test/
        ├── java/
        │   └── com/bookstore/
        │       ├── config/
        │       ├── data/
        │       │   ├── authors/
        │       │   └── books/
        │       ├── model/
        │       │   ├── authors/
        │       │   └── books/
        │       ├── steps/
        │       │   ├── AuthorsSteps.java
        │       │   ├── BaseSteps.java
        │       │   └── BooksSteps.java
        │       └── tests/
        │           ├── ApiTestBase.java
        │           ├── authors/
        │           └── books/
        └── resources/
            └── serenity.properties
```

## Architecture

The test framework is organized into the following layers:

```text
Tests
  ↓
Serenity step libraries
  ↓
REST Assured requests
  ↓
API under test
```

### Test classes

The test classes contain the scenarios and AssertJ validations:

- `ApiTestBase`
- `BooksApiTest`
- `AuthorsApiTest`

The `ApiTestBase` class contains steps initialization configuration as well as logging file attachment implementation to the report

### Step classes

The step classes contain the reusable REST operations:

- `BaseSteps`
- `BooksSteps`
- `AuthorsSteps`

`BaseSteps` creates and stores the shared REST Assured `RequestSpecification`. The resource-specific step classes inherit it.

### Model records

The API models use Java records:

- `Book`
- `BookRequest`
- `Author`
- `AuthorRequest`

Response bodies are converted into records using:

```java
Book actualBook = response.as(Book.class);
```

AssertJ is used to verify the resulting objects:

```java
assertThat(actualBook.id())
        .isEqualTo(expectedBookId);
```

### Test Data Classes
- `AuthorTestData`
- `BookTesstData`

This classes use lombok to generate payloads for the requests using the relevant Request classes

## Configuration

The default API configuration is stored in:

```text
src/test/resources/serenity.properties
```

```properties
api.base-url=https://fakerestapi.azurewebsites.net/
api.base-path=/api/v1
```

Running the tests without an override uses the configured default:

```bash
mvn clean verify
```

Run a single test with the following command

```bash
mvn clean verify -Dtest='TestClass#TestMethod'
```

A different base URL can be supplied for one execution:

```bash
mvn clean verify -DbaseUrl=https://qa.example.com
```

The Maven `baseUrl` system property takes precedence over
`api.base-url`.

GitHub Actions optionally reads the `BOOKS_API_BASE_URL` secret.
If the secret is configured, it is passed to Maven as `-DbaseUrl`.

If the secret is not configured, the workflow does not fail.
It runs Maven without `-DbaseUrl`, so the value from
`serenity.properties` is used.

The resource-specific paths are defined in the step classes:

```java
private static final String BOOKS_PATH = "/Books";
```

```java
private static final String AUTHORS_PATH = "/Authors";
```

## Prerequisites

Install the following:

- JDK 17 or newer
- Maven 3.9 or newer
- Git
- A running Books and Authors API

Verify the installed tools:

```bash
java -version
mvn -version
git --version
```