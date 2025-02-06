package com.lseg.digital.framework.qa.driver;

import org.openqa.selenium.WebDriver;
import com.lseg.digital.framework.qa.config.ConfigurationManager;

public class DriverManager {
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<ConfigurationManager> configManagerThreadLocal = new ThreadLocal<>();

    private DriverManager() {
        // Private constructor to prevent instantiation
    }

    public static synchronized WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            initializeDriver();
        }
        return driverThreadLocal.get();
    }

    public static synchronized void initializeDriver() {
        if (driverThreadLocal.get() != null) {
            quitDriver();
        }
        ConfigurationManager manager = new ConfigurationManager();
        configManagerThreadLocal.set(manager);
        driverThreadLocal.set(manager.initializeDriver());
    }

    public static synchronized void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                // Log the exception but don't throw it
                System.err.println("Error quitting driver: " + e.getMessage());
            } finally {
                driverThreadLocal.remove();
                configManagerThreadLocal.remove();
            }
        }
    }

    public static synchronized ConfigurationManager getConfigManager() {
        if (configManagerThreadLocal.get() == null) {
            configManagerThreadLocal.set(new ConfigurationManager());
        }
        return configManagerThreadLocal.get();
    }
} 