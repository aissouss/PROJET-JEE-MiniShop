package com.minishop.dao.impl;

import com.minishop.config.DbConfig;
import com.minishop.dao.ProductDao;
import com.minishop.model.Product;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JDBC implementation of ProductDao
 * Handles database operations for Product entity
 */
public class ProductDaoJdbc implements ProductDao {

    private static final Logger LOGGER = Logger.getLogger(ProductDaoJdbc.class.getName());

    private final DbConfig dbConfig;

    // SQL Queries
    private static final String SQL_FIND_ALL =
        "SELECT id, name, description, price_cents, stock, image_url, category, is_active, created_at, updated_at " +
        "FROM products ORDER BY created_at DESC";

    private static final String SQL_FIND_ALL_ACTIVE =
        "SELECT id, name, description, price_cents, stock, image_url, category, is_active, created_at, updated_at " +
        "FROM products WHERE is_active = TRUE ORDER BY created_at DESC";

    private static final String SQL_FIND_ALL_PAGINATED =
        "SELECT id, name, description, price_cents, stock, image_url, category, is_active, created_at, updated_at " +
        "FROM products ORDER BY created_at DESC LIMIT ? OFFSET ?";

    private static final String SQL_FIND_BY_ID =
        "SELECT id, name, description, price_cents, stock, image_url, category, is_active, created_at, updated_at " +
        "FROM products WHERE id = ?";

    private static final String SQL_FIND_BY_CATEGORY =
        "SELECT id, name, description, price_cents, stock, image_url, category, is_active, created_at, updated_at " +
        "FROM products WHERE category = ? AND is_active = TRUE ORDER BY name";

    private static final String SQL_SEARCH =
        "SELECT id, name, description, price_cents, stock, image_url, category, is_active, created_at, updated_at " +
        "FROM products WHERE (name LIKE ? OR description LIKE ?) AND is_active = TRUE ORDER BY name";

    private static final String SQL_FIND_LOW_STOCK =
        "SELECT id, name, description, price_cents, stock, image_url, category, is_active, created_at, updated_at " +
        "FROM products WHERE stock < ? ORDER BY stock ASC";

    private static final String SQL_INSERT =
        "INSERT INTO products (name, description, price_cents, stock, image_url, category, is_active) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
        "UPDATE products SET name = ?, description = ?, price_cents = ?, stock = ?, " +
        "image_url = ?, category = ?, is_active = ? WHERE id = ?";

    private static final String SQL_DELETE =
        "DELETE FROM products WHERE id = ?";

    private static final String SQL_UPDATE_STOCK =
        "UPDATE products SET stock = ? WHERE id = ?";

    private static final String SQL_DECREASE_STOCK =
        "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";

    private static final String SQL_COUNT =
        "SELECT COUNT(*) FROM products";

    private static final String SQL_COUNT_BY_CATEGORY =
        "SELECT COUNT(*) FROM products WHERE category = ?";

    private static final String SQL_GET_CATEGORIES =
        "SELECT DISTINCT category FROM products WHERE category IS NOT NULL ORDER BY category";

    public ProductDaoJdbc() {
        this.dbConfig = DbConfig.getInstance();
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding all products", e);
        }

        return products;
    }

    @Override
    public List<Product> findAllActive() {
        List<Product> products = new ArrayList<>();

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_FIND_ALL_ACTIVE);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding active products", e);
        }

        return products;
    }

    @Override
    public List<Product> findAll(int offset, int limit) {
        List<Product> products = new ArrayList<>();

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_FIND_ALL_PAGINATED)) {

            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding products with pagination", e);
        }

        return products;
    }

    @Override
    public Optional<Product> findById(long id) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_FIND_BY_ID)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToProduct(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding product by ID: " + id, e);
        }

        return Optional.empty();
    }

    @Override
    public List<Product> findByCategory(String category) {
        List<Product> products = new ArrayList<>();

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_FIND_BY_CATEGORY)) {

            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding products by category: " + category, e);
        }

        return products;
    }

    @Override
    public List<Product> search(String searchTerm) {
        List<Product> products = new ArrayList<>();

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SEARCH)) {

            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching products: " + searchTerm, e);
        }

        return products;
    }

    @Override
    public List<Product> findLowStock(int threshold) {
        List<Product> products = new ArrayList<>();

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_FIND_LOW_STOCK)) {

            stmt.setInt(1, threshold);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding low stock products", e);
        }

        return products;
    }

    @Override
    public Product create(Product product) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setInt(3, product.getPriceCents());
            stmt.setInt(4, product.getStock());
            stmt.setString(5, product.getImageUrl());
            stmt.setString(6, product.getCategory());
            stmt.setBoolean(7, product.isActive());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getLong(1));
                    LOGGER.info("Product created successfully with ID: " + product.getId());
                    return product;
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating product: " + product.getName(), e);
        }

        return null;
    }

    @Override
    public boolean update(Product product) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setInt(3, product.getPriceCents());
            stmt.setInt(4, product.getStock());
            stmt.setString(5, product.getImageUrl());
            stmt.setString(6, product.getCategory());
            stmt.setBoolean(7, product.isActive());
            stmt.setLong(8, product.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.info("Product updated successfully: " + product.getId());
                return true;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating product: " + product.getId(), e);
        }

        return false;
    }

    @Override
    public boolean delete(long id) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {

            stmt.setLong(1, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.info("Product deleted successfully: " + id);
                return true;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting product: " + id, e);
        }

        return false;
    }

    @Override
    public boolean updateStock(long productId, int newStock) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_STOCK)) {

            stmt.setInt(1, newStock);
            stmt.setLong(2, productId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.info("Product stock updated: " + productId + " -> " + newStock);
                return true;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating stock for product: " + productId, e);
        }

        return false;
    }

    @Override
    public boolean decreaseStock(long productId, int quantity) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_DECREASE_STOCK)) {

            stmt.setInt(1, quantity);
            stmt.setLong(2, productId);
            stmt.setInt(3, quantity);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                LOGGER.info("Product stock decreased: " + productId + " by " + quantity);
                return true;
            } else {
                LOGGER.warning("Could not decrease stock for product " + productId +
                             " - insufficient stock or product not found");
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error decreasing stock for product: " + productId, e);
        }

        return false;
    }

    @Override
    public long count() {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_COUNT);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error counting products", e);
        }

        return 0;
    }

    @Override
    public long countByCategory(String category) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_COUNT_BY_CATEGORY)) {

            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error counting products by category: " + category, e);
        }

        return 0;
    }

    @Override
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_GET_CATEGORIES);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categories.add(rs.getString("category"));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all categories", e);
        }

        return categories;
    }

    /**
     * Map a ResultSet row to a Product object
     */
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPriceCents(rs.getInt("price_cents"));
        product.setStock(rs.getInt("stock"));
        product.setImageUrl(rs.getString("image_url"));
        product.setCategory(rs.getString("category"));
        product.setActive(rs.getBoolean("is_active"));

        // Handle timestamps (may be null)
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            product.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            product.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return product;
    }
}
