package com.minishop.service;

import com.minishop.dao.ProductDao;
import com.minishop.dao.impl.ProductDaoJdbc;
import com.minishop.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Product service
 * Handles product business logic and operations
 */
public class ProductService {

    private static final Logger LOGGER = Logger.getLogger(ProductService.class.getName());
    private static ProductService instance;

    private final ProductDao productDao;

    private ProductService() {
        this.productDao = new ProductDaoJdbc();
    }

    /**
     * Get singleton instance of ProductService
     */
    public static synchronized ProductService getInstance() {
        if (instance == null) {
            instance = new ProductService();
        }
        return instance;
    }

    /**
     * Get all products
     *
     * @return List of all products
     */
    public List<Product> getAllProducts() {
        try {
            return productDao.findAll();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting all products", e);
            throw new RuntimeException("Failed to retrieve products", e);
        }
    }

    /**
     * Get all active products only
     *
     * @return List of active products
     */
    public List<Product> getAllActiveProducts() {
        try {
            return productDao.findAllActive();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting active products", e);
            throw new RuntimeException("Failed to retrieve active products", e);
        }
    }

    /**
     * Get products with pagination
     *
     * @param page     the page number (0-based)
     * @param pageSize the number of products per page
     * @return List of products for the requested page
     */
    public List<Product> getProducts(int page, int pageSize) {
        try {
            int offset = page * pageSize;
            return productDao.findAll(offset, pageSize);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting products with pagination", e);
            throw new RuntimeException("Failed to retrieve products", e);
        }
    }

    /**
     * Get a product by its ID
     *
     * @param id the product ID
     * @return Optional containing the product if found, empty otherwise
     */
    public Optional<Product> getProductById(long id) {
        try {
            return productDao.findById(id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting product by ID: " + id, e);
            return Optional.empty();
        }
    }

    /**
     * Get products by category
     *
     * @param category the category name
     * @return List of products in the category
     */
    public List<Product> getProductsByCategory(String category) {
        try {
            return productDao.findByCategory(category);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting products by category: " + category, e);
            throw new RuntimeException("Failed to retrieve products by category", e);
        }
    }

    /**
     * Search products by name or description
     *
     * @param searchTerm the search term
     * @return List of matching products
     */
    public List<Product> searchProducts(String searchTerm) {
        try {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return getAllActiveProducts();
            }
            return productDao.search(searchTerm.trim());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching products: " + searchTerm, e);
            throw new RuntimeException("Failed to search products", e);
        }
    }

    /**
     * Get products with low stock
     *
     * @param threshold the stock threshold
     * @return List of products with stock below threshold
     */
    public List<Product> getLowStockProducts(int threshold) {
        try {
            return productDao.findLowStock(threshold);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting low stock products", e);
            throw new RuntimeException("Failed to retrieve low stock products", e);
        }
    }

    /**
     * Create a new product
     *
     * @param product the product to create
     * @return the created product with generated ID
     */
    public Product createProduct(Product product) {
        try {
            // Validate product
            validateProduct(product);

            Product created = productDao.create(product);
            if (created != null) {
                LOGGER.info("Product created: " + created.getName());
                return created;
            } else {
                throw new RuntimeException("Failed to create product");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating product", e);
            throw new RuntimeException("Failed to create product", e);
        }
    }

    /**
     * Update an existing product
     *
     * @param product the product with updated information
     * @return true if update was successful, false otherwise
     */
    public boolean updateProduct(Product product) {
        try {
            // Validate product
            validateProduct(product);

            boolean updated = productDao.update(product);
            if (updated) {
                LOGGER.info("Product updated: " + product.getId());
            }
            return updated;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating product: " + product.getId(), e);
            throw new RuntimeException("Failed to update product", e);
        }
    }

    /**
     * Delete a product
     *
     * @param id the product ID
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteProduct(long id) {
        try {
            boolean deleted = productDao.delete(id);
            if (deleted) {
                LOGGER.info("Product deleted: " + id);
            }
            return deleted;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting product: " + id, e);
            throw new RuntimeException("Failed to delete product", e);
        }
    }

    /**
     * Update product stock
     *
     * @param productId the product ID
     * @param newStock  the new stock quantity
     * @return true if update was successful, false otherwise
     */
    public boolean updateStock(long productId, int newStock) {
        try {
            if (newStock < 0) {
                throw new IllegalArgumentException("Stock cannot be negative");
            }

            boolean updated = productDao.updateStock(productId, newStock);
            if (updated) {
                LOGGER.info("Stock updated for product " + productId + ": " + newStock);
            }
            return updated;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating stock for product: " + productId, e);
            throw new RuntimeException("Failed to update stock", e);
        }
    }

    /**
     * Decrease product stock (for order processing)
     *
     * @param productId the product ID
     * @param quantity  the quantity to decrease
     * @return true if stock was decreased, false otherwise
     */
    public boolean decreaseStock(long productId, int quantity) {
        try {
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be positive");
            }

            boolean decreased = productDao.decreaseStock(productId, quantity);
            if (decreased) {
                LOGGER.info("Stock decreased for product " + productId + " by " + quantity);
            } else {
                LOGGER.warning("Could not decrease stock for product " + productId +
                             " - insufficient stock");
            }
            return decreased;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error decreasing stock for product: " + productId, e);
            throw new RuntimeException("Failed to decrease stock", e);
        }
    }

    /**
     * Get total product count
     *
     * @return total number of products
     */
    public long getTotalProductCount() {
        try {
            return productDao.count();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error counting products", e);
            return 0;
        }
    }

    /**
     * Get all categories
     *
     * @return List of category names
     */
    public List<String> getAllCategories() {
        try {
            return productDao.getAllCategories();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting categories", e);
            throw new RuntimeException("Failed to retrieve categories", e);
        }
    }

    /**
     * Validate product data
     */
    private void validateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }

        if (product.getName().length() > 255) {
            throw new IllegalArgumentException("Product name is too long (max 255 characters)");
        }

        if (product.getPriceCents() < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }

        if (product.getStock() < 0) {
            throw new IllegalArgumentException("Product stock cannot be negative");
        }
    }

    /**
     * Check if product is available for purchase
     *
     * @param productId the product ID
     * @param quantity  the desired quantity
     * @return true if product is available, false otherwise
     */
    public boolean isProductAvailable(long productId, int quantity) {
        Optional<Product> productOpt = getProductById(productId);
        if (productOpt.isEmpty()) {
            return false;
        }

        Product product = productOpt.get();
        return product.isActive() && product.getStock() >= quantity;
    }
}
