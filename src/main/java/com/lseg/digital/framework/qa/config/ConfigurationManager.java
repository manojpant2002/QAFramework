package com.lseg.digital.framework.qa.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.safari.SafariDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;

public class ConfigurationManager {
    private static final String DEFAULT_BROWSER = "chrome";
    private static final String DEFAULT_ENV = "qa";
    private static final String DEFAULT_EXPLICIT_TIMEOUT = "10";
    private static final String DEFAULT_IMPLICIT_TIMEOUT = "5";
    private static final String DEFAULT_PAGE_LOAD_TIMEOUT = "30";

    private Browser browser;
    private Environment environment;
    private String baseUrl;
    private String apiBaseUrl;
    private Duration explicitWaitTimeout;
    private Duration implicitWaitTimeout;
    private Duration pageLoadTimeout;

    public ConfigurationManager() {
        initializeConfiguration();
    }

    private void initializeConfiguration() {
        String browserName = System.getProperty("browser", DEFAULT_BROWSER);
        String env = System.getProperty("env", DEFAULT_ENV);
        
        this.browser = Browser.fromString(browserName);
        this.environment = Environment.fromString(env);
        
        // Get URLs from system properties (set by Maven profiles)
        this.baseUrl = System.getProperty("baseUrl");
        this.apiBaseUrl = System.getProperty("apiBaseUrl");

        // Initialize timeouts
        String explicitTimeout = System.getProperty("explicit.wait.timeout", DEFAULT_EXPLICIT_TIMEOUT);
        String implicitTimeout = System.getProperty("implicit.wait.timeout", DEFAULT_IMPLICIT_TIMEOUT);
        String pageLoadTimeoutStr = System.getProperty("page.load.timeout", DEFAULT_PAGE_LOAD_TIMEOUT);

        this.explicitWaitTimeout = Duration.ofSeconds(Long.parseLong(explicitTimeout));
        this.implicitWaitTimeout = Duration.ofSeconds(Long.parseLong(implicitTimeout));
        this.pageLoadTimeout = Duration.ofSeconds(Long.parseLong(pageLoadTimeoutStr));
    }

    public WebDriver initializeDriver() {
        WebDriver driver;
        
        switch (browser) {
            case CHROME:
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
                
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
                
            case EDGE:
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
                
            case SAFARI:
                // Safari doesn't need WebDriverManager
                driver = new SafariDriver();
                break;
                
            case UC:
                // Add UC Browser implementation
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(); // Using ChromeDriver as placeholder
                break;
                
            default:
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
        }
        
        // Set timeouts
        driver.manage().timeouts().implicitlyWait(implicitWaitTimeout);
        driver.manage().timeouts().pageLoadTimeout(pageLoadTimeout);
        driver.manage().window().maximize();
        
        return driver;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public Browser getBrowser() {
        return browser;
    }

    // Add getters for timeouts
    public Duration getExplicitWaitTimeout() {
        return explicitWaitTimeout;
    }

    public Duration getImplicitWaitTimeout() {
        return implicitWaitTimeout;
    }

    public Duration getPageLoadTimeout() {
        return pageLoadTimeout;
    }
} 