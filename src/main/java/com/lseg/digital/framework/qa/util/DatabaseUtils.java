package com.lseg.digital.framework.qa.util;

import java.sql.*;
import java.util.Properties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DatabaseUtils {
    private static final HikariDataSource dataSource;
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    static {
        try {
            Properties props = new Properties();
            props.load(DatabaseUtils.class.getClassLoader().getResourceAsStream("jdbc.properties"));
            
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("jdbc.url"));
            config.setUsername(props.getProperty("jdbc.user"));
            config.setPassword(props.getProperty("jdbc.password"));
            config.setMaximumPoolSize(5);
            
            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize connection pool", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static ResultSet executeQuery(String sql, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeQuery();
        }
    }

    public static int executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeUpdate();
        }
    }

    public static Future<ResultSet> executeQueryAsync(String sql, Object... params) {
        return executor.submit(() -> executeQuery(sql, params));
    }
    
    public static Future<Integer> executeUpdateAsync(String sql, Object... params) {
        return executor.submit(() -> executeUpdate(sql, params));
    }
} 