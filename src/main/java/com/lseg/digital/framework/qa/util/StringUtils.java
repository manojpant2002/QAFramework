package com.lseg.digital.framework.qa.util;

import org.apache.commons.lang3.RandomStringUtils;

public class StringUtils {
    
    public static String generateRandomString(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public static String maskSensitiveData(String input) {
        if (input == null || input.length() < 4) return "****";
        return input.substring(0, 2) + "****" + input.substring(input.length() - 2);
    }
} 