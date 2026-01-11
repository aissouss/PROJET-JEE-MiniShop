package com.minishop.dao.impl;

import com.minishop.config.DbConfig;
import com.minishop.dao.UserDao;
import com.minishop.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JDBC implementation of UserDao.
 */
public class UserDaoJdbc implements UserDao {

    private static final Logger LOGGER = Logger.getLogger(UserDaoJdbc.class.getName());

    private static final String SQL_FIND_BY_EMAIL =
        "SELECT * FROM users WHERE email = ?";

    private final DbConfig dbConfig;

    public UserDaoJdbc() {
        this.dbConfig = new DbConfig();
    }

    @Override
    public User findByEmail(String email) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_FIND_BY_EMAIL)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding user by email: " + email, e);
        }
        return null;
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
