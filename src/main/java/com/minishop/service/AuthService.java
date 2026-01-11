package com.minishop.service;

import com.minishop.dao.UserDao;
import com.minishop.dao.impl.UserDaoJdbc;
import com.minishop.model.User;
import com.minishop.util.PasswordUtil;

import java.util.Optional;
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
            Optional<User> userOptional = userDao.findByEmail(email.trim().toLowerCase());
            if (userOptional.isEmpty()) {
                return null;
            }

            User user = userOptional.get();
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

    /**
     * Register a new user (optional feature).
     */
    public Optional<User> register(String email, String password, String fullName) {
        if (!isValidEmail(email) || password == null || password.isEmpty()
            || fullName == null || fullName.trim().isEmpty()) {
            return Optional.empty();
        }

        String normalizedEmail = email.trim().toLowerCase();

        if (userDao.existsByEmail(normalizedEmail)) {
            return Optional.empty();
        }

        try {
            User user = new User();
            user.setEmail(normalizedEmail);
            user.setPasswordHash(PasswordUtil.sha256(password));
            user.setFullName(fullName.trim());
            user.setRole("USER");

            User savedUser = userDao.save(user);
            return Optional.ofNullable(savedUser);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during registration for: " + normalizedEmail, e);
            return Optional.empty();
        }
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}
