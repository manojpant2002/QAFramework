package com.lseg.digital.framework.qa.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/resources/features/ui",
    glue = {"com.lseg.digital.framework.qa.stepdefs.ui"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports/ui-report.html"
    }
)
public class UITestRunner extends AbstractTestNGCucumberTests {
} 