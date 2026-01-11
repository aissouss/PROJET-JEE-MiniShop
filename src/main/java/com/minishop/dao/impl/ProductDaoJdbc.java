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
}
