package com.minishop.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database configuration and connection management
 */
public class DbConfig {

    private static DbConfig instance;
    private Connection connection;

    /**
     * Private constructor for singleton pattern
     */
    private DbConfig() {
        try {
            // Load MySQL JDBC driver
            Class.forName(AppConstants.DB_DRIVER);

            // Establish connection
            this.connection = DriverManager.getConnection(
                AppConstants.DB_URL,
                AppConstants.DB_USERNAME,
                AppConstants.DB_PASSWORD
            );

            System.out.println("Database connection established successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            throw new RuntimeException("Failed to load database driver", e);
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection: " + e.getMessage());
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    /**
     * Get singleton instance of DbConfig
     *
     * @return DbConfig instance
     */
    public static synchronized DbConfig getInstance() {
        if (instance == null) {
            instance = new DbConfig();
        }
        return instance;
    }

    /**
     * Get database connection
     *
     * @return Connection object
     * @throws SQLException if connection is closed or invalid
     */
    public Connection getConnection() throws SQLException {
        // Check if connection is still valid, reconnect if needed
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                AppConstants.DB_URL,
                AppConstants.DB_USERNAME,
                AppConstants.DB_PASSWORD
            );
        }
        return connection;
    }

    /**
     * Close database connection
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }

    /**
     * Test database connection
     *
     * @return true if connection is valid, false otherwise
     */
    public boolean testConnection() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(2);
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
            return false;
        }
    }
}
