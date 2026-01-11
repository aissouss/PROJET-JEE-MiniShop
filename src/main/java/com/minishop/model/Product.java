package com.minishop.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Product entity representing a product in the catalog.
 */
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private String description;
    private int priceCents;
    private int stock;

    public Product() {
    }

    public Product(String name, String description, int priceCents, int stock) {
        this.name = name;
        this.description = description;
        this.priceCents = priceCents;
        this.stock = stock;
    }

    public Product(long id, String name, String description, int priceCents, int stock) {
        this(name, description, priceCents, stock);
        this.id = id;
    }

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

    public String getFormattedPrice() {
        return String.format("%.2f €", priceCents / 100.0);
    }

    public String getStockStatus() {
        if (stock <= 0) {
            return "Rupture de stock";
        }
        if (stock < 5) {
            return "Stock limité (" + stock + " restants)";
        }
        return "En stock";
    }

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
                '}';
    }
}
