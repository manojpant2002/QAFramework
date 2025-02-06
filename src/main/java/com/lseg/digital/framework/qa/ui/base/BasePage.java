package com.lseg.digital.framework.qa.ui.base;

import com.lseg.digital.framework.qa.driver.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, DriverManager.getConfigManager().getExplicitWaitTimeout());
        PageFactory.initElements(driver, this);
    }

    protected String getBaseUrl() {
        return DriverManager.getConfigManager().getBaseUrl();
    }
} 