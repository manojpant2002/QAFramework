# Test Automation Framework

A comprehensive test automation framework supporting UI, API, and Performance testing.

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Node.js 14+ (for k6 performance tests)
- k6 installed globally (`npm install -g k6`)

## Framework Features

- Selenium WebDriver for UI testing
- REST Assured for API testing
- k6 for Performance testing
- Cucumber BDD
- Parallel execution support
- Automatic screenshot capture on failures
- Excel-based object repository
- Environment-specific configurations
- Detailed logging
- Cross-browser testing


## Running Tests

### Available Maven Profiles

#### Browser Profiles
- `chrome` (default): Run tests using Chrome browser
- `firefox`: Run tests using Firefox browser
- `edge`: Run tests using Edge browser
- `safari`: Run tests using Safari browser
- `uc`: Run tests using UndetectedChromeDriver

#### Environment Profiles
- `qa` (default): Run tests against QA environment
- `uat`: Run tests against UAT environment
- `ppe`: Run tests against PPE environment
- `prod`: Run tests against Production environment

####Run API tests with default profile (QA)R
-  mvn clean test -DtestNG.suite.file=src/test/resources/testng-api.xml
###Run API tests against specific environment
-  mvn clean test -DtestNG.suite.file=src/test/resources/testng-api.xml -P uat
   
####Run UI tests with default profiles (Chrome, QA)
- mvn clean test -DtestNG.suite.file=src/test/resources/testng-ui.xml -P chrome,qa
####Run UI tests with different browser and environment
- mvn clean test -DtestNG.suite.file=src/test/resources/testng-ui.xml -P firefox,uat

K6 Performance Test
k6 run \
  -e BASE_URL=https://qa.example.com \
  -e VUS=10000 \
  -e DURATION=1s \
  -e ENDPOINT=/api/health \
  -e COOLDOWN=30 \
  login-performance.js