package com.lseg.digital.framework.qa.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.time.Duration;

@Slf4j
public class DriverManager {
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<ConfigManager> configManagerThreadLocal = new ThreadLocal<>();

    public static void initializeDriver() {
        if (driverThreadLocal.get() != null) {
            log.warn("Driver is already initialized for current thread");
            return;
        }

        String browser = System.getProperty("browser", "chrome").toLowerCase();
        log.info("Initializing {} driver", browser);

        WebDriver driver;
        try {
            switch (browser) {
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    break;
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    driver = new EdgeDriver();
                    break;
                case "safari":
                    driver = new SafariDriver();
                    break;
                case "chrome":
                default:
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver();
            }

            // Configure timeouts
            ConfigManager configManager = getConfigManager();
            driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(configManager.getImplicitWaitTimeout()));
            driver.manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(configManager.getPageLoadTimeout()));
            driver.manage().window().maximize();

            driverThreadLocal.set(driver);
            log.info("Driver initialized successfully");

        } catch (Exception e) {
            log.error("Failed to initialize driver", e);
            throw new RuntimeException("Failed to initialize driver: " + e.getMessage());
        }
    }

    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            log.error("Driver not initialized. Call initializeDriver() first");
            throw new IllegalStateException("Driver not initialized. Call initializeDriver() first");
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                log.info("Driver quit successfully");
            } catch (Exception e) {
                log.error("Error while quitting driver", e);
            } finally {
                driverThreadLocal.remove();
                configManagerThreadLocal.remove();
                log.debug("Cleaned up thread-local resources");
            }
        }
    }

    public static ConfigManager getConfigManager() {
        ConfigManager configManager = configManagerThreadLocal.get();
        if (configManager == null) {
            configManager = new ConfigManager();
            configManagerThreadLocal.set(configManager);
        }
        return configManager;
    }

    private DriverManager() {
        // Private constructor to prevent instantiation
    }
} 