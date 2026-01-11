package com.minishop.dao.impl;

import com.minishop.config.DbConfig;
import com.minishop.dao.UserDao;
import com.minishop.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JDBC implementation of UserDao.
 */
public class UserDaoJdbc implements UserDao {

    private static final Logger LOGGER = Logger.getLogger(UserDaoJdbc.class.getName());

    private static final String SQL_FIND_BY_EMAIL =
        "SELECT id, email, password_hash, full_name, role FROM users WHERE email = ?";

    private static final String SQL_INSERT =
        "INSERT INTO users (email, password_hash, full_name, role) VALUES (?, ?, ?, ?)";

    private static final String SQL_EXISTS_BY_EMAIL =
        "SELECT COUNT(*) FROM users WHERE email = ?";

    private final DbConfig dbConfig;

    public UserDaoJdbc() {
        this.dbConfig = DbConfig.getInstance();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_FIND_BY_EMAIL)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding user by email: " + email, e);
        }
        return Optional.empty();
    }

    @Override
    public User save(User user) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getRole());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                    return user;
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving user: " + user.getEmail(), e);
        }

        return null;
    }

    @Override
    public boolean existsByEmail(String email) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_EXISTS_BY_EMAIL)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking if user exists: " + email, e);
        }

        return false;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFullName(rs.getString("full_name"));
        user.setRole(rs.getString("role"));
        return user;
    }
}
