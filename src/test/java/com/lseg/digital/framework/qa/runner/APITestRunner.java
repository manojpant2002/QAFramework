package com.lseg.digital.framework.qa.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
    features = "src/test/resources/features/api",
    glue = "com.lseg.digital.framework.qa.stepdefs",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/api/cucumber-pretty",
        "json:target/cucumber-reports/api/CucumberTestReport.json"
    }
)
public class APITestRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
} 