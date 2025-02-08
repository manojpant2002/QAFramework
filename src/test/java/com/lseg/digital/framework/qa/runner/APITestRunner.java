package com.lseg.digital.framework.qa.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;

@Slf4j
@CucumberOptions(
    features = "src/test/resources/features/api",
    glue = {"com.lseg.digital.framework.qa.stepdefs.api"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports/api-report.html",
        "json:target/cucumber-reports/api-report.json"
    }
)
public class APITestRunner extends AbstractTestNGCucumberTests {
    
    @BeforeClass(alwaysRun = true)
    public void setup() {
        log.info("Starting API Test Execution");
    }
} 