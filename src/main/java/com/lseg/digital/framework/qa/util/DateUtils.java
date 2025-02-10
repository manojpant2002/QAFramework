package com.lseg.digital.framework.qa.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    
    public static String getCurrentDateTime(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime parseDateTime(String dateString, String pattern) {
        return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(pattern));
    }

    public static long getTimestamp() {
        return Instant.now().getEpochSecond();
    }
} 