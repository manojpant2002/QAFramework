package com.lseg.digital.framework.qa.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/resources/features/api",
    glue = {"com.lseg.digital.framework.qa.stepdefs.api"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports/api-report.html"
    }
)
public class APITestRunner extends AbstractTestNGCucumberTests {
} 