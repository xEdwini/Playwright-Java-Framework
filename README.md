# SauceDemo Playwright Framework — Java

A production-style Playwright framework implementing the SauceDemo smoke and regression packs.

## Technology

- Java 17
- Maven
- Playwright Java 1.60.0
- JUnit 5
- Page Object Model
- SLF4J and Logback
- Allure JUnit 5
- Parallel class and method execution
- Screenshots and Playwright traces on failure
- Environment-variable overrides

## Setup

```bash
mvn -q test-compile
mvn exec:java -e \
  -Dexec.mainClass=com.microsoft.playwright.CLI \
  -Dexec.args="install --with-deps"
```

A simpler option after dependencies are downloaded:

```bash
mvn test
```

Playwright Java downloads browser binaries automatically when the CLI installation command is run.

## Run

```bash
mvn test
mvn test -Dgroups=smoke
mvn test -Dgroups=regression
mvn test -Dgroups=login
```

Run headed:

```bash
HEADLESS=false mvn test -Dgroups=smoke
```

Use another browser:

```bash
BROWSER=firefox mvn test
BROWSER=webkit mvn test
```

Override the target URL:

```bash
BASE_URL=https://www.saucedemo.com/ mvn test
```

## Allure

```bash
mvn allure:report
mvn allure:serve
```

Results are written to `target/allure-results`.

## Artifacts

- `target/artifacts/screenshots`
- `target/artifacts/traces`
- `target/artifacts/videos`
- `target/artifacts/logs`
- `target/allure-results`
- `target/surefire-reports`

## Design notes

- Every test creates a fresh BrowserContext for isolation.
- Page Objects contain locators and user actions.
- Tests contain assertions and business intent.
- Playwright auto-waiting is used instead of fixed sleeps.
- JUnit tags separate smoke, regression, login, inventory and cart/checkout suites.
- Manual test-case IDs are preserved in Allure `@Story` metadata.
