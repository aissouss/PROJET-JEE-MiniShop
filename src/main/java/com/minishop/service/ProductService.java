package com.minishop.service;

import com.minishop.dao.ProductDao;
import com.minishop.dao.impl.ProductDaoJdbc;
import com.minishop.model.Product;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Product service.
 */
public class ProductService {

    private static final Logger LOGGER = Logger.getLogger(ProductService.class.getName());
    private static ProductService instance;

    private final ProductDao productDao;

    private ProductService() {
        this.productDao = new ProductDaoJdbc();
    }

    public static synchronized ProductService getInstance() {
        if (instance == null) {
            instance = new ProductService();
        }
        return instance;
    }

    public List<Product> getAllProducts() {
        try {
            return productDao.findAll();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting all products", e);
            throw new RuntimeException("Failed to retrieve products", e);
        }
    }

    public Product getProductById(long id) {
        try {
            return productDao.findById(id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting product by ID: " + id, e);
            return null;
        }
    }

    // Admin methods

    public void createProduct(Product product) {
        try {
            productDao.create(product);
            LOGGER.info("Admin created product: " + product.getName());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating product", e);
            throw new RuntimeException("Failed to create product", e);
        }
    }

    public void updateProduct(Product product) {
        try {
            productDao.update(product);
            LOGGER.info("Admin updated product: " + product.getName());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating product", e);
            throw new RuntimeException("Failed to update product", e);
        }
    }

    public void deleteProduct(long id) {
        try {
            productDao.delete(id);
            LOGGER.info("Admin deleted product ID: " + id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting product", e);
            throw new RuntimeException("Failed to delete product", e);
        }
    }
}
