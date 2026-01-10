package com.minishop.service;

import com.minishop.dao.UserDao;
import com.minishop.dao.impl.UserDaoJdbc;
import com.minishop.model.User;
import com.minishop.util.PasswordUtil;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Authentication service
 * Handles user authentication and registration logic
 */
public class AuthService {

    private static final Logger LOGGER = Logger.getLogger(AuthService.class.getName());
    private static AuthService instance;

    private final UserDao userDao;

    private AuthService() {
        this.userDao = new UserDaoJdbc();
    }

    /**
     * Get singleton instance of AuthService
     */
    public static synchronized AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    /**
     * Authenticate a user with email and password
     *
     * @param email    the user's email
     * @param password the user's plaintext password
     * @return Optional containing the authenticated user if successful, empty otherwise
     */
    public Optional<User> authenticate(String email, String password) {
        if (email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            LOGGER.warning("Authentication attempt with empty credentials");
            return Optional.empty();
        }

        try {
            Optional<User> userOptional = userDao.findByEmail(email.trim().toLowerCase());

            if (userOptional.isEmpty()) {
                LOGGER.info("Authentication failed: user not found - " + email);
                return Optional.empty();
            }

            User user = userOptional.get();

            // Check if user account is active
            if (!user.isActive()) {
                LOGGER.warning("Authentication failed: account inactive - " + email);
                return Optional.empty();
            }

            // Verify password (using MD5 to match database sample data)
            String hashedPassword = PasswordUtil.hashPasswordMD5(password);

            if (!hashedPassword.equals(user.getPasswordHash())) {
                LOGGER.info("Authentication failed: invalid password - " + email);
                return Optional.empty();
            }

            // Update last login timestamp
            userDao.updateLastLogin(user.getId());

            LOGGER.info("User authenticated successfully: " + email);
            return Optional.of(user);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during authentication for: " + email, e);
            return Optional.empty();
        }
    }

    /**
     * Register a new user
     *
     * @param email    the user's email
     * @param password the user's plaintext password
     * @param fullName the user's full name
     * @return Optional containing the registered user if successful, empty otherwise
     */
    public Optional<User> register(String email, String password, String fullName) {
        // Validate input
        if (!isValidEmail(email)) {
            LOGGER.warning("Registration failed: invalid email - " + email);
            return Optional.empty();
        }

        if (!isValidPassword(password)) {
            LOGGER.warning("Registration failed: invalid password");
            return Optional.empty();
        }

        if (fullName == null || fullName.trim().isEmpty()) {
            LOGGER.warning("Registration failed: empty full name");
            return Optional.empty();
        }

        String normalizedEmail = email.trim().toLowerCase();

        // Check if email already exists
        if (userDao.existsByEmail(normalizedEmail)) {
            LOGGER.warning("Registration failed: email already exists - " + normalizedEmail);
            return Optional.empty();
        }

        try {
            // Create new user
            User user = new User();
            user.setEmail(normalizedEmail);
            user.setPasswordHash(PasswordUtil.hashPasswordMD5(password));
            user.setFullName(fullName.trim());
            user.setRole(User.UserRole.USER);
            user.setActive(true);

            // Save to database
            User savedUser = userDao.save(user);

            if (savedUser != null) {
                LOGGER.info("User registered successfully: " + normalizedEmail);
                return Optional.of(savedUser);
            } else {
                LOGGER.warning("Registration failed: could not save user - " + normalizedEmail);
                return Optional.empty();
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during registration for: " + normalizedEmail, e);
            return Optional.empty();
        }
    }

    /**
     * Change user password
     *
     * @param userId          the user's ID
     * @param currentPassword the current password
     * @param newPassword     the new password
     * @return true if password was changed successfully, false otherwise
     */
    public boolean changePassword(Long userId, String currentPassword, String newPassword) {
        Optional<User> userOptional = userDao.findById(userId);

        if (userOptional.isEmpty()) {
            LOGGER.warning("Password change failed: user not found - " + userId);
            return false;
        }

        User user = userOptional.get();

        // Verify current password
        String hashedCurrentPassword = PasswordUtil.hashPasswordMD5(currentPassword);
        if (!hashedCurrentPassword.equals(user.getPasswordHash())) {
            LOGGER.warning("Password change failed: invalid current password - " + userId);
            return false;
        }

        // Validate new password
        if (!isValidPassword(newPassword)) {
            LOGGER.warning("Password change failed: invalid new password - " + userId);
            return false;
        }

        // Update password
        user.setPasswordHash(PasswordUtil.hashPasswordMD5(newPassword));
        boolean updated = userDao.update(user);

        if (updated) {
            LOGGER.info("Password changed successfully for user: " + userId);
        }

        return updated;
    }

    /**
     * Check if user has admin role
     *
     * @param user the user to check
     * @return true if user is admin, false otherwise
     */
    public boolean isAdmin(User user) {
        return user != null && user.getRole() == User.UserRole.ADMIN;
    }

    /**
     * Validate email format
     */
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Validate password requirements
     * Minimum 6 characters for demo purposes (use stronger requirements in production)
     */
    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    /**
     * Get user by ID
     */
    public Optional<User> getUserById(Long userId) {
        return userDao.findById(userId);
    }

    /**
     * Get user by email
     */
    public Optional<User> getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }
}
