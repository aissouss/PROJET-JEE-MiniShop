package com.minishop.dao;

import com.minishop.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for User entity
 * Defines CRUD operations and custom queries for users
 */
public interface UserDao {

    /**
     * Find a user by their ID
     *
     * @param id the user ID
     * @return Optional containing the user if found, empty otherwise
     */
    Optional<User> findById(Long id);

    /**
     * Find a user by their email address
     *
     * @param email the user email
     * @return Optional containing the user if found, empty otherwise
     */
    Optional<User> findByEmail(String email);

    /**
     * Find all users in the system
     *
     * @return List of all users
     */
    List<User> findAll();

    /**
     * Find all users with pagination
     *
     * @param offset the starting position
     * @param limit  the maximum number of records to return
     * @return List of users for the requested page
     */
    List<User> findAll(int offset, int limit);

    /**
     * Find users by role
     *
     * @param role the user role
     * @return List of users with the specified role
     */
    List<User> findByRole(User.UserRole role);

    /**
     * Save a new user to the database
     *
     * @param user the user to save
     * @return the saved user with generated ID
     */
    User save(User user);

    /**
     * Update an existing user
     *
     * @param user the user with updated information
     * @return true if update was successful, false otherwise
     */
    boolean update(User user);

    /**
     * Update user's last login timestamp
     *
     * @param userId the user ID
     * @return true if update was successful, false otherwise
     */
    boolean updateLastLogin(Long userId);

    /**
     * Delete a user by ID
     *
     * @param id the user ID
     * @return true if deletion was successful, false otherwise
     */
    boolean delete(Long id);

    /**
     * Check if a user exists with the given email
     *
     * @param email the email to check
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Count total number of users
     *
     * @return total user count
     */
    long count();

    /**
     * Activate or deactivate a user account
     *
     * @param userId the user ID
     * @param active true to activate, false to deactivate
     * @return true if update was successful, false otherwise
     */
    boolean setActive(Long userId, boolean active);
}
