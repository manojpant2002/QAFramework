package com.lseg.digital.framework.qa.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
    features = "src/test/resources/features/ui",
    glue = "com.lseg.digital.framework.qa.stepdefs",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/ui/cucumber-pretty",
        "json:target/cucumber-reports/ui/CucumberTestReport.json"
    }
)
public class UITestRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
} 