package com.minishop.dao;

import com.minishop.model.Product;

import java.util.List;
/**
 * Data Access Object interface for Product entity.
 */
public interface ProductDao {

    List<Product> findAll();

    Product findById(long id);
}
