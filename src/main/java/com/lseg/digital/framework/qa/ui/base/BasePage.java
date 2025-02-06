package com.lseg.digital.framework.qa.ui.base;

import com.lseg.digital.framework.qa.driver.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import lombok.extern.slf4j.Slf4j;
import java.time.Duration;

@Slf4j
public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected final String pageName;

    public BasePage(WebDriver driver) {
        log.debug("Initializing BasePage with driver: {}", driver.getClass().getSimpleName());
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DriverManager.getConfigManager().getExplicitWaitTimeout()));
        this.pageName = getClass().getSimpleName();
        PageFactory.initElements(driver, this);
        log.debug("BasePage initialized for page: {}", pageName);
    }

    protected String getPageName() {
        return pageName;
    }

    protected String getBaseUrl() {
        String baseUrl = DriverManager.getConfigManager().getBaseUrl();
        log.debug("Retrieved base URL: {}", baseUrl);
        return baseUrl;
    }
} 