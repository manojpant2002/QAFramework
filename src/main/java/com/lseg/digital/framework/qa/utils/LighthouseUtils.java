package com.lseg.digital.framework.qa.utils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v120.page.Page;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LighthouseUtils {
    private static final String LIGHTHOUSE_DIR = "test-output/lighthouse-reports";
    private static final ConcurrentHashMap<Long, Path> threadReportDirs = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, DevTools> threadDevTools = new ConcurrentHashMap<>();
    private static final AtomicInteger reportCounter = new AtomicInteger(0);
    
    public static synchronized void generateLighthouseReport(ChromeDriver driver, String pageName) {
        Long threadId = Thread.currentThread().getId();
        try {
            Path reportDir = getThreadReportDirectory();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String reportName = String.format("%s_thread-%d_%s_%d_lighthouse.html", 
                pageName,
                threadId,
                timestamp,
                reportCounter.incrementAndGet()
            );
            Path reportPath = reportDir.resolve(reportName);

            // Get or create DevTools session for this thread
            DevTools devTools = threadDevTools.computeIfAbsent(threadId, k -> {
                DevTools dt = driver.getDevTools();
                dt.createSession();
                return dt;
            });

            // Configure Lighthouse parameters
            Map<String, Object> params = new HashMap<>();
            params.put("format", "html");
            params.put("categories", new String[]{"performance", "accessibility", "best-practices", "seo"});
            
            // Execute Lighthouse audit with thread-specific configuration
            synchronized (devTools) {
                devTools.send(Page.enable());
                String lighthouseResult = driver.executeCdpCommand("Lighthouse.getReport", params).toString();
                Files.write(reportPath, lighthouseResult.getBytes());
            }
            
            log.info("Thread {} - Lighthouse report generated: {}", threadId, reportPath);

            // Log performance metrics for this thread
            driver.manage().logs().get(LogType.PERFORMANCE).getAll().stream()
                .filter(entry -> entry.getMessage().contains("Lighthouse"))
                .forEach(entry -> log.debug("Thread {} - Performance log: {}", threadId, entry.getMessage()));

        } catch (Exception e) {
            log.error("Thread {} - Failed to generate Lighthouse report", threadId, e);
        }
    }

    private static Path getThreadReportDirectory() throws IOException {
        return threadReportDirs.computeIfAbsent(Thread.currentThread().getId(), id -> {
            try {
                Path threadDir = Paths.get(LIGHTHOUSE_DIR, "thread-" + id);
                Files.createDirectories(threadDir);
                log.debug("Created Lighthouse report directory for thread {}: {}", id, threadDir);
                return threadDir;
            } catch (IOException e) {
                log.error("Failed to create Lighthouse report directory for thread {}", id, e);
                throw new RuntimeException(e);
            }
        });
    }

    public static void cleanup() {
        Long threadId = Thread.currentThread().getId();
        try {
            // Cleanup DevTools session
            DevTools devTools = threadDevTools.remove(threadId);
            if (devTools != null) {
                try {
                    devTools.close();
                } catch (Exception e) {
                    log.warn("Thread {} - Error closing DevTools session", threadId, e);
                }
            }
            
            // Cleanup directory mapping
            threadReportDirs.remove(threadId);
            log.debug("Thread {} - Cleaned up Lighthouse resources", threadId);
            
        } catch (Exception e) {
            log.error("Thread {} - Error during cleanup", threadId, e);
        }
    }
} 