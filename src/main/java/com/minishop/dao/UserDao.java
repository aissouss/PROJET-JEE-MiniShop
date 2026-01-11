package com.minishop.dao;

import com.minishop.model.User;

/**
 * Data Access Object interface for User entity.
 */
public interface UserDao {

    /**
     * Find a user by their email address.
     */
    User findByEmail(String email);
}
