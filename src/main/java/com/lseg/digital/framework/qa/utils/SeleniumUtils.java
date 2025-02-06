package com.lseg.digital.framework.qa.utils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

@Slf4j
public class SeleniumUtils {
    
    /**
     * Safely sends keys to an element after clearing existing text
     */
    public static void sendKeys(WebDriver driver, WebElement element, String text) {
        try {
            waitForElementToBeClickable(driver, element);
            highlightElement(driver, element);
            element.clear();
            element.sendKeys(text);
            log.debug("Sent text '{}' to element: {}", text, element);
        } catch (Exception e) {
            log.error("Failed to send keys to element: {}", element, e);
            throw e;
        }
    }

    /**
     * Safely clicks an element
     */
    public static void click(WebDriver driver, WebElement element) {
        try {
            waitForElementToBeClickable(driver, element);
            highlightElement(driver, element);
            element.click();
            log.debug("Clicked element: {}", element);
        } catch (Exception e) {
            log.error("Failed to click element: {}", element, e);
            throw e;
        }
    }

    /**
     * Selects option by visible text from dropdown
     */
    public static void selectByVisibleText(WebDriver driver, WebElement element, String text) {
        try {
            waitForElementToBeVisible(driver, element);
            highlightElement(driver, element);
            Select select = new Select(element);
            select.selectByVisibleText(text);
            log.debug("Selected option '{}' from dropdown: {}", text, element);
        } catch (Exception e) {
            log.error("Failed to select option '{}' from dropdown: {}", text, element, e);
            throw e;
        }
    }

    /**
     * Scrolls element into view
     */
    public static void scrollIntoView(WebDriver driver, WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            log.debug("Scrolled element into view: {}", element);
        } catch (Exception e) {
            log.error("Failed to scroll element into view: {}", element, e);
            throw e;
        }
    }

    /**
     * Scrolls vertically by pixels
     */
    public static void scrollVertically(WebDriver driver, int pixels) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                "window.scrollBy(0, arguments[0]);", pixels);
            log.debug("Scrolled vertically by {} pixels", pixels);
        } catch (Exception e) {
            log.error("Failed to scroll vertically", e);
            throw e;
        }
    }

    /**
     * Scrolls horizontally by pixels
     */
    public static void scrollHorizontally(WebDriver driver, int pixels) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                "window.scrollBy(arguments[0], 0);", pixels);
            log.debug("Scrolled horizontally by {} pixels", pixels);
        } catch (Exception e) {
            log.error("Failed to scroll horizontally", e);
            throw e;
        }
    }

    /**
     * Hovers over an element
     */
    public static void hoverOverElement(WebDriver driver, WebElement element) {
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(element).perform();
            log.debug("Hovered over element: {}", element);
        } catch (Exception e) {
            log.error("Failed to hover over element: {}", element, e);
            throw e;
        }
    }

    /**
     * Waits for element to be clickable
     */
    public static void waitForElementToBeClickable(WebDriver driver, WebElement element) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(element));
            log.debug("Element is clickable: {}", element);
        } catch (Exception e) {
            log.error("Element not clickable: {}", element, e);
            throw e;
        }
    }

    /**
     * Waits for element to be visible
     */
    public static void waitForElementToBeVisible(WebDriver driver, WebElement element) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(element));
            log.debug("Element is visible: {}", element);
        } catch (Exception e) {
            log.error("Element not visible: {}", element, e);
            throw e;
        }
    }

    /**
     * Checks if element is displayed
     */
    public static boolean isElementDisplayed(WebElement element) {
        try {
            boolean isDisplayed = element.isDisplayed();
            log.debug("Element display status: {} for element: {}", isDisplayed, element);
            return isDisplayed;
        } catch (Exception e) {
            log.debug("Element is not displayed: {}", element);
            return false;
        }
    }

    /**
     * Gets text from element
     */
    public static String getText(WebElement element) {
        try {
            String text = element.getText();
            log.debug("Got text '{}' from element: {}", text, element);
            return text;
        } catch (Exception e) {
            log.error("Failed to get text from element: {}", element, e);
            throw e;
        }
    }

    /**
     * Switches to frame
     */
    public static void switchToFrame(WebDriver driver, WebElement frameElement) {
        try {
            driver.switchTo().frame(frameElement);
            log.debug("Switched to frame: {}", frameElement);
        } catch (Exception e) {
            log.error("Failed to switch to frame: {}", frameElement, e);
            throw e;
        }
    }

    /**
     * Switches to default content
     */
    public static void switchToDefaultContent(WebDriver driver) {
        try {
            driver.switchTo().defaultContent();
            log.debug("Switched to default content");
        } catch (Exception e) {
            log.error("Failed to switch to default content", e);
            throw e;
        }
    }

    /**
     * Accepts alert
     */
    public static void acceptAlert(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
            log.debug("Accepted alert");
        } catch (Exception e) {
            log.error("Failed to accept alert", e);
            throw e;
        }
    }

    /**
     * Dismisses alert
     */
    public static void dismissAlert(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().dismiss();
            log.debug("Dismissed alert");
        } catch (Exception e) {
            log.error("Failed to dismiss alert", e);
            throw e;
        }
    }

    /**
     * Highlights element for better visibility during test execution
     */
    private static void highlightElement(WebDriver driver, WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                "arguments[0].style.border='3px solid red'", element);
            Thread.sleep(100);
            js.executeScript(
                "arguments[0].style.border=''", element);
        } catch (Exception e) {
            log.warn("Failed to highlight element: {}", element, e);
            // Don't throw exception as this is just a visual aid
        }
    }

    /**
     * Checks if checkbox is selected
     */
    public static boolean isCheckboxSelected(WebElement checkbox) {
        try {
            boolean isSelected = checkbox.isSelected();
            log.debug("Checkbox selection status: {} for element: {}", isSelected, checkbox);
            return isSelected;
        } catch (Exception e) {
            log.error("Failed to check checkbox status: {}", checkbox, e);
            throw e;
        }
    }

    /**
     * Gets all options from select dropdown
     */
    public static List<WebElement> getSelectOptions(WebElement element) {
        try {
            Select select = new Select(element);
            List<WebElement> options = select.getOptions();
            log.debug("Got {} options from select element: {}", options.size(), element);
            return options;
        } catch (Exception e) {
            log.error("Failed to get options from select element: {}", element, e);
            throw e;
        }
    }
} 