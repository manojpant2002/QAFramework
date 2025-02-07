package com.lseg.digital.framework.qa.base;

import com.lseg.digital.framework.qa.driver.DriverManager;
import com.lseg.digital.framework.qa.utils.ScreenshotUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class BaseTest {
    
    @Before
    public void setUp() {
        log.info("Initializing test");
        DriverManager.initializeDriver();
    }

    @After(order = 1) // Higher order runs first
    public void captureFailureScreenshot(Scenario scenario) {
        if (scenario.isFailed()) {
            log.info("Scenario failed, capturing screenshot");
            String screenshotPath = ScreenshotUtils.captureScreenshot(
                DriverManager.getDriver(), 
                scenario.getName()
            );
            if (screenshotPath != null) {
                try {
                    byte[] screenshot = Files.readAllBytes(Paths.get(screenshotPath));
                    scenario.attach(screenshot, "image/png", "Screenshot");
                } catch (IOException e) {
                    log.error("Failed to attach screenshot to scenario", e);
                }
            }
        }
    }

    @After(order = 2) // Lower order runs last
    public void tearDown() {
        log.info("Cleaning up test resources");
        ScreenshotUtils.cleanup();  // Clean up screenshot resources
        DriverManager.quitDriver();
    }
} 