package com.lseg.digital.framework.qa.ui.base;

import com.lseg.digital.framework.qa.driver.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        log.debug("Initializing BasePage with driver: {}", driver.getClass().getSimpleName());
        this.driver = driver;
        this.wait = new WebDriverWait(driver, DriverManager.getConfigManager().getExplicitWaitTimeout());
        PageFactory.initElements(driver, this);
        log.debug("BasePage initialized with explicit wait timeout: {} seconds", 
            DriverManager.getConfigManager().getExplicitWaitTimeout());
    }

    protected String getBaseUrl() {
        String baseUrl = DriverManager.getConfigManager().getBaseUrl();
        log.debug("Retrieved base URL: {}", baseUrl);
        return baseUrl;
    }
} 