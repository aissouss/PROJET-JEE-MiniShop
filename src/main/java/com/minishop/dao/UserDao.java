package com.minishop.dao;

import com.minishop.model.User;

import java.util.Optional;

/**
 * Data Access Object interface for User entity.
 */
public interface UserDao {

    /**
     * Find a user by their email address.
     */
    Optional<User> findByEmail(String email);

    /**
     * Save a new user to the database.
     */
    User save(User user);

    /**
     * Check if a user exists with the given email.
     */
    boolean existsByEmail(String email);
}
