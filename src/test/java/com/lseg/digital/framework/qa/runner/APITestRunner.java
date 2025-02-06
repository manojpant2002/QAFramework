package com.lseg.digital.framework.qa.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

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
} 