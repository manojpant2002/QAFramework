package com.lseg.digital.framework.qa.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FileUtils {
    private static final ExecutorService ioExecutor = Executors.newWorkStealingPool();
    
    public static String readFileAsString(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    public static String getResourceFileContent(String fileName) throws IOException {
        ClassLoader classLoader = FileUtils.class.getClassLoader();
        return new String(classLoader.getResourceAsStream(fileName).readAllBytes());
    }

    public static Future<String> readFileAsync(Path path) {
        return ioExecutor.submit(() -> new String(Files.readAllBytes(path)));
    }
    
    public static Future<Path> writeFileAsync(Path path, String content) {
        return ioExecutor.submit(() -> Files.write(path, content.getBytes()));
    }
} 