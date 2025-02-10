package com.lseg.digital.framework.qa.util;

import net.javacrumbs.jsonunit.JsonAssert;
import net.javacrumbs.jsonunit.core.Configuration;
import net.javacrumbs.jsonunit.core.Option;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class JsonComparator {
    
    public static void assertJsonEquals(String expected, String actual, String... ignorePaths) {
        JsonAssert.assertJsonEquals(
            expected,
            actual,
            JsonAssert.when(Option.IGNORING_ARRAY_ORDER)
                     .whenIgnoringPaths(ignorePaths)
        );
    }

    public static boolean isJsonSimilar(String expected, String actual, String... ignorePaths) {
        try {
            assertJsonEquals(expected, actual, ignorePaths);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    public static Configuration whenIgnoring(String... paths) {
        return JsonAssert.when(Option.IGNORING_ARRAY_ORDER)
                        .whenIgnoringPaths(paths);
    }

    public static Future<Boolean> isJsonSimilarAsync(String expected, String actual, String... ignorePaths) {
        return CompletableFuture.supplyAsync(() -> isJsonSimilar(expected, actual, ignorePaths));
    }
} 