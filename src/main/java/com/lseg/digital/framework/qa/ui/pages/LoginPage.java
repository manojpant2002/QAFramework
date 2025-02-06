package com.lseg.digital.framework.qa.ui.pages;

import com.lseg.digital.framework.qa.ui.base.BasePage;
import com.lseg.digital.framework.qa.utils.ExcelObjectRepository;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginPage extends BasePage {

    private WebElement getUsernameInput() {
        log.debug("Getting username input element");
        return findElement(ExcelObjectRepository.getLocator(pageName, "username"));
    }

    private WebElement getPasswordInput() {
        log.debug("Getting password input element");
        return findElement(ExcelObjectRepository.getLocator(pageName, "password"));
    }

    private WebElement getLoginButton() {
        log.debug("Getting login button element");
        return findElement(ExcelObjectRepository.getLocator(pageName, "loginButton"));
    }

    public LoginPage(WebDriver driver) {
        super(driver);
        log.info("LoginPage initialized");
    }

    public void login(String username, String password) {
        log.info("Attempting login with username: {}", username);
        getUsernameInput().sendKeys(username);
        getPasswordInput().sendKeys(password);
        log.debug("Clicking login button");
        getLoginButton().click();
        log.info("Login attempt completed");
    }

    private WebElement findElement(String locator) {
        log.debug("Finding element with locator: {}", locator);
        String[] parts = locator.split("=", 2);
        String type = parts[0];
        String value = parts[1];

        WebElement element;
        try {
            switch (type.toLowerCase()) {
                case "id":
                    element = driver.findElement(By.id(value));
                    break;
                case "name":
                    element = driver.findElement(By.name(value));
                    break;
                case "xpath":
                    element = driver.findElement(By.xpath(value));
                    break;
                case "css":
                    element = driver.findElement(By.cssSelector(value));
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported locator type: " + type);
            }
            log.debug("Element found successfully with {}: {}", type, value);
            return element;
        } catch (Exception e) {
            log.error("Failed to find element with {}: {}", type, value, e);
            throw e;
        }
    }
} 