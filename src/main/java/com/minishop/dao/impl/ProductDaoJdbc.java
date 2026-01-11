package com.minishop.dao.impl;

import com.minishop.config.DbConfig;
import com.minishop.dao.ProductDao;
import com.minishop.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JDBC implementation of ProductDao.
 */
public class ProductDaoJdbc implements ProductDao {

    private static final Logger LOGGER = Logger.getLogger(ProductDaoJdbc.class.getName());

    private static final String SQL_FIND_ALL =
        "SELECT id, name, description, price_cents, stock FROM products ORDER BY created_at DESC";

    private static final String SQL_FIND_BY_ID =
        "SELECT id, name, description, price_cents, stock FROM products WHERE id = ?";

    private final DbConfig dbConfig;

    public ProductDaoJdbc() {
        this.dbConfig = new DbConfig();
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
    public Product findById(long id) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_FIND_BY_ID)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToProduct(rs);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding product by ID: " + id, e);
        }

        return null;
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPriceCents(rs.getInt("price_cents"));
        product.setStock(rs.getInt("stock"));
        return product;
    }

    // Admin CRUD methods

    @Override
    public void create(Product product) {
        String sql = "INSERT INTO products (name, description, price_cents, stock) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setInt(3, product.getPriceCents());
            stmt.setInt(4, product.getStock());

            int rowsAffected = stmt.executeUpdate();
            LOGGER.info("Product created: " + product.getName() + " (rows: " + rowsAffected + ")");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating product", e);
            throw new RuntimeException("Failed to create product", e);
        }
    }

    @Override
    public void update(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, price_cents = ?, stock = ? WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setInt(3, product.getPriceCents());
            stmt.setInt(4, product.getStock());
            stmt.setLong(5, product.getId());

            int rowsAffected = stmt.executeUpdate();
            LOGGER.info("Product updated: " + product.getName() + " (rows: " + rowsAffected + ")");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating product ID: " + product.getId(), e);
            throw new RuntimeException("Failed to update product", e);
        }
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            int rowsAffected = stmt.executeUpdate();
            LOGGER.info("Product deleted: ID " + id + " (rows: " + rowsAffected + ")");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting product ID: " + id, e);
            throw new RuntimeException("Failed to delete product", e);
        }
    }
}
