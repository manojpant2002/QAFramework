package com.lseg.digital.framework.qa.config;

public enum Browser {
    CHROME,
    FIREFOX,
    EDGE,
    SAFARI,
    UC;

    public static Browser fromString(String browser) {
        return browser != null ? valueOf(browser.toUpperCase()) : CHROME;
    }
} 