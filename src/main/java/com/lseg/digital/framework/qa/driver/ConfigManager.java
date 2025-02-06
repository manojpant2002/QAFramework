package com.lseg.digital.framework.qa.driver;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class ConfigManager {
    private final String baseUrl;
    private final String apiBaseUrl;
    private final long explicitWaitTimeout;
    private final long implicitWaitTimeout;
    private final long pageLoadTimeout;

    public ConfigManager() {
        this.baseUrl = System.getProperty("baseUrl");
        this.apiBaseUrl = System.getProperty("apiBaseUrl");
        this.explicitWaitTimeout = Long.parseLong(
            System.getProperty("explicit.wait.timeout", "10"));
        this.implicitWaitTimeout = Long.parseLong(
            System.getProperty("implicit.wait.timeout", "5"));
        this.pageLoadTimeout = Long.parseLong(
            System.getProperty("page.load.timeout", "30"));
        
        log.debug("Configuration initialized with baseUrl: {}, apiBaseUrl: {}", 
            baseUrl, apiBaseUrl);
    }
} 