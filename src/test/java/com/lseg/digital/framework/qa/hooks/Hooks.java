package com.lseg.digital.framework.qa.hooks;

import com.lseg.digital.framework.qa.driver.DriverManager;
import com.lseg.digital.framework.qa.utils.ScreenshotUtils;
import com.lseg.digital.framework.qa.utils.LighthouseUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class Hooks {
    
    @Before
    public void setUp() {
        log.info("Initializing test");
        DriverManager.initializeDriver();
    }

    @After(order = 1)
    public void generateReports(Scenario scenario) {
        WebDriver driver = DriverManager.getDriver();
        if (driver instanceof ChromeDriver) {
            log.info("Generating Lighthouse report");
            LighthouseUtils.generateLighthouseReport((ChromeDriver) driver, scenario.getName());
        }
        
        if (scenario.isFailed()) {
            log.info("Scenario failed, capturing screenshot");
            String screenshotPath = ScreenshotUtils.captureScreenshot(driver, scenario.getName());
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

    @After(order = 2)
    public void tearDown() {
        log.info("Cleaning up test resources");
        LighthouseUtils.cleanup();
        ScreenshotUtils.cleanup();
        DriverManager.quitDriver();
    }
} 