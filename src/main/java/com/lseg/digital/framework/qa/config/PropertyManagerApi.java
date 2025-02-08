package com.lseg.digital.framework.qa.config;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyManagerApi {
    private static final Logger logger = LoggerFactory.getLogger(PropertyManagerApi.class);
    @Getter
    private final Properties properties;
    private final String propertyFile;
   public PropertyManagerApi(String propertyFile) {
        this.propertyFile = propertyFile;
        this.properties = new Properties();
        loadProperties();
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(propertyFile)) {
            if (input == null) {
                try (FileInputStream fileInput = new FileInputStream(propertyFile)) {
                    properties.load(fileInput);
                }
            } else {
                properties.load(input);
            }
            logger.info("Thread {} - Loaded API properties from: {}", 
                Thread.currentThread().getId(), propertyFile);
        } catch (IOException e) {
            logger.error("Thread {} - Failed to load API properties from: {}", 
                Thread.currentThread().getId(), propertyFile, e);
            throw new RuntimeException("Failed to load API configuration", e);
        }
    }
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}