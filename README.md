# API Test Automation Framework

A robust API testing framework built with REST Assured, TestNG, and Cucumber for testing LSEG Digital APIs.

## 🛠 Tech Stack

- Java 11
- REST Assured 5.4.0
- JUnit
- Cucumber 7.15.0
- Maven
- Lombok
- SLF4J & Logback


## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher


## Framework Features

- REST Assured for API testing
- Cucumber BDD
- Parallel execution support
- Environment-specific configurations
- Detailed logging


### Available Maven Profiles

#### Environment Profiles
- `qa` (default): Run tests against QA environment
- `uat`: Run tests against UAT environment
- `ppe`: Run tests against PPE environment
- `dev`: Run tests against Production environment

####Run API tests with default profile (QA)
-  mvn clean integration-test [ will run apiqa profile , which is activated by default ]

###Run API tests against specific environment
-  mvn clean integration-test -Papiqa -P apiqa
-  mvn clean integration-test -Papiqa -P apidev
-  mvn clean integration-test -Papiqa -P apiuat
-  mvn clean integration-test -Papiqa -P apippe

### Sample filtering
Mixed operators - Run (smoke OR regression) AND NOT flaky
mvn clean test -Dcucumber.filter.tags="(@smoke or @regression) and not @flaky"
mvn clean test -Dcucumber.filter.tags="(@quick or @sanity) and (@core or @critical) and not @deprecated"
mvn clean integration-test -Papiqa -Dcucumber.filter.tags="@auth and @token and @validation"
   

## Test Reports

- Cucumber Reports: `target/cucumber-reports`

  Reports are generated per test and stored in thread-specific directories for parallel execution support.