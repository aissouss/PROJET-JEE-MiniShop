package com.minishop.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Cart item representing a product in the shopping cart
 */
public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private Product product;
    private int quantity;

    // Constructors
    public CartItem() {
    }

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    // Business methods

    /**
     * Get total price in cents for this cart item (price * quantity)
     */
    public int getTotalCents() {
        return product.getPriceCents() * quantity;
    }

    /**
     * Get total price in euros
     */
    public double getTotalEuros() {
        return getTotalCents() / 100.0;
    }

    /**
     * Get formatted total price with currency symbol
     */
    public String getFormattedTotal() {
        return String.format("%.2f €", getTotalEuros());
    }

    /**
     * Increase quantity by specified amount
     */
    public void increaseQuantity(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        this.quantity += amount;
    }

    /**
     * Decrease quantity by specified amount
     */
    public void decreaseQuantity(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (this.quantity < amount) {
            throw new IllegalArgumentException("Cannot decrease quantity below 0");
        }
        this.quantity -= amount;
    }

    /**
     * Check if quantity is valid based on product stock
     */
    public boolean isQuantityValid() {
        return quantity > 0 && quantity <= product.getStock();
    }

    /**
     * Get maximum quantity that can be added (based on stock)
     */
    public int getMaxQuantity() {
        return product.getStock();
    }

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(product.getId(), cartItem.product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(product.getId());
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "product=" + product.getName() +
                ", quantity=" + quantity +
                ", total=" + getFormattedTotal() +
                '}';
    }
}
