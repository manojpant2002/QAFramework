package com.lseg.digital.framework.qa.runner;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/api")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.lseg.digital.framework.qa.stepdefs.api")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-reports/cucumber.html, json:target/cucumber-reports/cucumber.json, usage:target/cucumber-reports/usage.txt")
@ConfigurationParameter(key = PARALLEL_EXECUTION_ENABLED_PROPERTY_NAME, value = "true")
@ConfigurationParameter(
        key = FILTER_TAGS_PROPERTY_NAME,
        value = "${cucumber.filter.tags:@api or not @wip}"  // or , and , not 
)
@ConfigurationParameter(key = JUNIT_PLATFORM_NAMING_STRATEGY_PROPERTY_NAME, value = "long")
@ConfigurationParameter(
    key = "cucumber.execution.allow-started-options", 
    value = "false"  // Fails execution if invalid tags are specified
)
public class APITestRunner {
} 