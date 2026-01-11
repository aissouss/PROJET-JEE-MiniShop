package com.minishop.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database configuration and connection management.
 */
public class DbConfig {

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            AppConstants.DB_URL,
            AppConstants.DB_USERNAME,
            AppConstants.DB_PASSWORD
        );
    }
}
