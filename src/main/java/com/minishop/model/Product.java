package com.minishop.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Product entity representing a product in the catalog
 */
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private String description;
    private int priceCents;
    private int stock;
    private String imageUrl;
    private String category;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public Product() {
        this.active = true;
    }

    public Product(String name, String description, int priceCents, int stock) {
        this();
        this.name = name;
        this.description = description;
        this.priceCents = priceCents;
        this.stock = stock;
    }

    public Product(long id, String name, String description, int priceCents, int stock) {
        this(name, description, priceCents, stock);
        this.id = id;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriceCents() {
        return priceCents;
    }

    public void setPriceCents(int priceCents) {
        this.priceCents = priceCents;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Helper methods
    /**
     * Get price in euros (formatted)
     */
    public double getPriceEuros() {
        return priceCents / 100.0;
    }

    /**
     * Get formatted price with currency symbol
     */
    public String getFormattedPrice() {
        return String.format("%.2f €", getPriceEuros());
    }

    /**
     * Check if product is in stock
     */
    public boolean isInStock() {
        return stock > 0;
    }

    /**
     * Check if product is available (active and in stock)
     */
    public boolean isAvailable() {
        return active && isInStock();
    }

    /**
     * Get stock status message
     */
    public String getStockStatus() {
        if (!active) {
            return "Produit non disponible";
        }
        if (stock == 0) {
            return "Rupture de stock";
        }
        if (stock < 5) {
            return "Stock limité (" + stock + " restants)";
        }
        return "En stock";
    }

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", priceCents=" + priceCents +
                ", stock=" + stock +
                ", category='" + category + '\'' +
                ", active=" + active +
                '}';
    }
}
