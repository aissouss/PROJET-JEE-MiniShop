package com.minishop.dao.impl;

import com.minishop.config.DbConfig;
import com.minishop.dao.UserDao;
import com.minishop.model.User;
import com.minishop.model.User.UserRole;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JDBC implementation of UserDao
 * Handles database operations for User entity
 */
public class UserDaoJdbc implements UserDao {

    private static final Logger LOGGER = Logger.getLogger(UserDaoJdbc.class.getName());

    private final DbConfig dbConfig;

    // SQL Queries
    private static final String SQL_FIND_BY_ID =
        "SELECT id, email, password_hash, full_name, role, created_at, updated_at, last_login, is_active " +
        "FROM users WHERE id = ?";

    private static final String SQL_FIND_BY_EMAIL =
        "SELECT id, email, password_hash, full_name, role, created_at, updated_at, last_login, is_active " +
        "FROM users WHERE email = ?";

    private static final String SQL_FIND_ALL =
        "SELECT id, email, password_hash, full_name, role, created_at, updated_at, last_login, is_active " +
        "FROM users ORDER BY created_at DESC";

    private static final String SQL_FIND_ALL_PAGINATED =
        "SELECT id, email, password_hash, full_name, role, created_at, updated_at, last_login, is_active " +
        "FROM users ORDER BY created_at DESC LIMIT ? OFFSET ?";

    private static final String SQL_FIND_BY_ROLE =
        "SELECT id, email, password_hash, full_name, role, created_at, updated_at, last_login, is_active " +
        "FROM users WHERE role = ? ORDER BY created_at DESC";

    private static final String SQL_INSERT =
        "INSERT INTO users (email, password_hash, full_name, role, is_active) VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
        "UPDATE users SET email = ?, password_hash = ?, full_name = ?, role = ?, is_active = ? WHERE id = ?";

    private static final String SQL_UPDATE_LAST_LOGIN =
        "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE id = ?";

    private static final String SQL_DELETE =
        "DELETE FROM users WHERE id = ?";

    private static final String SQL_EXISTS_BY_EMAIL =
        "SELECT COUNT(*) FROM users WHERE email = ?";

    private static final String SQL_COUNT =
        "SELECT COUNT(*) FROM users";

    private static final String SQL_SET_ACTIVE =
        "UPDATE users SET is_active = ? WHERE id = ?";

    public UserDaoJdbc() {
        this.dbConfig = DbConfig.getInstance();
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_FIND_BY_ID)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding user by ID: " + id, e);
        }
        return Optional.empty();
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
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding all users", e);
        }

        return users;
    }

    @Override
    public List<User> findAll(int offset, int limit) {
        List<User> users = new ArrayList<>();

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_FIND_ALL_PAGINATED)) {

            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding users with pagination", e);
        }

        return users;
    }

    @Override
    public List<User> findByRole(UserRole role) {
        List<User> users = new ArrayList<>();

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_FIND_BY_ROLE)) {

            stmt.setString(1, role.name());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding users by role: " + role, e);
        }

        return users;
    }

    @Override
    public User save(User user) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getRole().name());
            stmt.setBoolean(5, user.isActive());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                    LOGGER.info("User created successfully with ID: " + user.getId());
                    return user;
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving user: " + user.getEmail(), e);
        }

        return null;
    }

    @Override
    public boolean update(User user) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {

            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getRole().name());
            stmt.setBoolean(5, user.isActive());
            stmt.setLong(6, user.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.info("User updated successfully: " + user.getId());
                return true;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user: " + user.getId(), e);
        }

        return false;
    }

    @Override
    public boolean updateLastLogin(Long userId) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_LAST_LOGIN)) {

            stmt.setLong(1, userId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating last login for user: " + userId, e);
        }

        return false;
    }

    @Override
    public boolean delete(Long id) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {

            stmt.setLong(1, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.info("User deleted successfully: " + id);
                return true;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting user: " + id, e);
        }

        return false;
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
            LOGGER.log(Level.SEVERE, "Error checking if email exists: " + email, e);
        }

        return false;
    }

    @Override
    public long count() {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_COUNT);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error counting users", e);
        }

        return 0;
    }

    @Override
    public boolean setActive(Long userId, boolean active) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SET_ACTIVE)) {

            stmt.setBoolean(1, active);
            stmt.setLong(2, userId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.info("User active status updated: " + userId + " -> " + active);
                return true;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error setting active status for user: " + userId, e);
        }

        return false;
    }

    /**
     * Map a ResultSet row to a User object
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFullName(rs.getString("full_name"));
        user.setRole(UserRole.valueOf(rs.getString("role")));
        user.setActive(rs.getBoolean("is_active"));

        // Handle timestamps (may be null)
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            user.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        Timestamp lastLogin = rs.getTimestamp("last_login");
        if (lastLogin != null) {
            user.setLastLogin(lastLogin.toLocalDateTime());
        }

        return user;
    }
}
