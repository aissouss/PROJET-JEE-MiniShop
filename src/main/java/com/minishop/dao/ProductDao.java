package com.minishop.dao;

import com.minishop.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Product entity.
 */
public interface ProductDao {

    List<Product> findAll();

    Optional<Product> findById(long id);
}
