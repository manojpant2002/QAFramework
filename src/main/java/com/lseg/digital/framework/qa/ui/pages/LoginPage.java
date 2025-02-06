package com.lseg.digital.framework.qa.ui.pages;

import com.lseg.digital.framework.qa.ui.base.BasePage;
import com.lseg.digital.framework.qa.utils.ExcelObjectRepository;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {
    private static final String PAGE_NAME = "LoginPage";

    private WebElement getUsernameInput() {
        return findElement(ExcelObjectRepository.getLocator(PAGE_NAME, "username"));
    }

    private WebElement getPasswordInput() {
        return findElement(ExcelObjectRepository.getLocator(PAGE_NAME, "password"));
    }

    private WebElement getLoginButton() {
        return findElement(ExcelObjectRepository.getLocator(PAGE_NAME, "loginButton"));
    }

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login(String username, String password) {
        getUsernameInput().sendKeys(username);
        getPasswordInput().sendKeys(password);
        getLoginButton().click();
    }

    private WebElement findElement(String locator) {
        String[] parts = locator.split("=", 2);
        String type = parts[0];
        String value = parts[1];

        switch (type.toLowerCase()) {
            case "id":
                return driver.findElement(By.id(value));
            case "name":
                return driver.findElement(By.name(value));
            case "xpath":
                return driver.findElement(By.xpath(value));
            case "css":
                return driver.findElement(By.cssSelector(value));
            default:
                throw new IllegalArgumentException("Unsupported locator type: " + type);
        }
    }
} 