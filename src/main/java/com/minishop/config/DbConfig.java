package com.minishop.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Database configuration and connection management.
 */
public class DbConfig {

    private static final Logger LOGGER = Logger.getLogger(DbConfig.class.getName());
    
    static {
        // Charger le driver MySQL au démarrage de la classe
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            LOGGER.info("MySQL JDBC Driver chargé avec succès");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Impossible de charger le driver MySQL JDBC", e);
            throw new RuntimeException("Driver MySQL introuvable", e);
        }
    }

    public Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(
                AppConstants.DB_URL,
                AppConstants.DB_USERNAME,
                AppConstants.DB_PASSWORD
            );
            LOGGER.info("Connexion MySQL établie avec succès");
            return conn;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Échec de connexion à MySQL", e);
            throw e;
        }
    }
}
