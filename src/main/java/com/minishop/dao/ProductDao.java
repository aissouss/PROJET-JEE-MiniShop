package com.minishop.dao;

import com.minishop.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Product entity
 * Defines CRUD operations and custom queries for products
 */
public interface ProductDao {

    /**
     * Find all products
     *
     * @return List of all products
     */
    List<Product> findAll();

    /**
     * Find all active products only
     *
     * @return List of active products
     */
    List<Product> findAllActive();

    /**
     * Find all products with pagination
     *
     * @param offset the starting position
     * @param limit  the maximum number of records to return
     * @return List of products for the requested page
     */
    List<Product> findAll(int offset, int limit);

    /**
     * Find a product by its ID
     *
     * @param id the product ID
     * @return Optional containing the product if found, empty otherwise
     */
    Optional<Product> findById(long id);

    /**
     * Find products by category
     *
     * @param category the product category
     * @return List of products in the specified category
     */
    List<Product> findByCategory(String category);

    /**
     * Search products by name or description
     *
     * @param searchTerm the search term
     * @return List of matching products
     */
    List<Product> search(String searchTerm);

    /**
     * Find products with low stock (below threshold)
     *
     * @param threshold the stock threshold
     * @return List of products with low stock
     */
    List<Product> findLowStock(int threshold);

    /**
     * Create a new product
     *
     * @param product the product to create
     * @return the created product with generated ID
     */
    Product create(Product product);

    /**
     * Update an existing product
     *
     * @param product the product with updated information
     * @return true if update was successful, false otherwise
     */
    boolean update(Product product);

    /**
     * Delete a product by ID
     *
     * @param id the product ID
     * @return true if deletion was successful, false otherwise
     */
    boolean delete(long id);

    /**
     * Update product stock
     *
     * @param productId the product ID
     * @param newStock  the new stock quantity
     * @return true if update was successful, false otherwise
     */
    boolean updateStock(long productId, int newStock);

    /**
     * Decrease product stock by quantity
     *
     * @param productId the product ID
     * @param quantity  the quantity to decrease
     * @return true if update was successful, false otherwise
     */
    boolean decreaseStock(long productId, int quantity);

    /**
     * Count total number of products
     *
     * @return total product count
     */
    long count();

    /**
     * Count products by category
     *
     * @param category the category
     * @return count of products in category
     */
    long countByCategory(String category);

    /**
     * Get all distinct categories
     *
     * @return List of category names
     */
    List<String> getAllCategories();
}
