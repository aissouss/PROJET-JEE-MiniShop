package com.minishop.service;

import com.minishop.dao.UserDao;
import com.minishop.dao.impl.UserDaoJdbc;
import com.minishop.model.User;
import com.minishop.util.PasswordUtil;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Authentication service.
 */
public class AuthService {

    private static final Logger LOGGER = Logger.getLogger(AuthService.class.getName());
    private static AuthService instance;

    private final UserDao userDao;

    private AuthService() {
        this.userDao = new UserDaoJdbc();
    }

    public static synchronized AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    /**
     * Authenticate a user with email and password.
     *
     * @return the authenticated User or null if invalid.
     */
    public User login(String email, String passwordPlain) {
        if (email == null || email.trim().isEmpty() || passwordPlain == null || passwordPlain.isEmpty()) {
            return null;
        }

        try {
            User user = userDao.findByEmail(email.trim().toLowerCase());
            if (user == null) {
                return null;
            }

            String hashed = PasswordUtil.sha256(passwordPlain);
            if (!hashed.equals(user.getPasswordHash())) {
                return null;
            }

            return user;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during authentication for: " + email, e);
            return null;
        }
    }
}
