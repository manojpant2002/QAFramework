package com.lseg.digital.framework.qa.utils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ScreenshotUtils {
    private static final ConcurrentHashMap<Long, Path> threadScreenshotDirs = new ConcurrentHashMap<>();
    private static final String BASE_SCREENSHOT_DIR = "test-output/screenshots";

    public static String captureScreenshot(WebDriver driver, String testName) {
        try {
            Path threadDir = getThreadScreenshotDirectory();
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = String.format("%s_%s_%s.png", 
                testName,
                Thread.currentThread().getId(),
                timestamp
            );
            Path filePath = threadDir.resolve(fileName);

            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(screenshot.toPath(), filePath);

            log.info("Screenshot captured: {}", filePath);
            return filePath.toString();
        } catch (Exception e) {
            log.error("Failed to capture screenshot", e);
            return null;
        }
    }

    private static Path getThreadScreenshotDirectory() {
        return threadScreenshotDirs.computeIfAbsent(Thread.currentThread().getId(), id -> {
            try {
                Path threadDir = Paths.get(BASE_SCREENSHOT_DIR, "thread-" + id);
                Files.createDirectories(threadDir);
                log.debug("Created screenshot directory for thread {}: {}", id, threadDir);
                return threadDir;
            } catch (Exception e) {
                log.error("Failed to create screenshot directory for thread {}", id, e);
                throw new RuntimeException(e);
            }
        });
    }

    public static void cleanup() {
        Long threadId = Thread.currentThread().getId();
        threadScreenshotDirs.remove(threadId);
        log.debug("Cleaned up screenshot directory mapping for thread {}", threadId);
    }
} 