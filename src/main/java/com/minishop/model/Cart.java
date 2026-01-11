package com.minishop.model;

import java.io.Serializable;
import java.util.*;

/**
 * Shopping cart containing cart items
 * Stored in HTTP session
 */
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<Long, CartItem> items;

    // Constructor
    public Cart() {
        this.items = new HashMap<>();
    }

    // Business methods

    /**
     * Add a product to the cart
     * If product already exists, increase quantity
     *
     * @param product  the product to add
     * @param quantity the quantity to add
     */
    public void addProduct(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (quantity > product.getStock()) {
            throw new IllegalArgumentException("Quantity exceeds available stock");
        }

        long productId = product.getId();

        if (items.containsKey(productId)) {
            // Product already in cart - increase quantity
            CartItem existingItem = items.get(productId);
            int newQuantity = existingItem.getQuantity() + quantity;

            // Check if new quantity exceeds stock
            if (newQuantity > product.getStock()) {
                throw new IllegalArgumentException("Total quantity exceeds available stock");
            }

            existingItem.setQuantity(newQuantity);
        } else {
            // New product - create cart item
            CartItem newItem = new CartItem(product, quantity);
            items.put(productId, newItem);
        }
    }

    /**
     * Update quantity for a product in the cart
     *
     * @param productId the product ID
     * @param quantity  the new quantity
     */
    public void updateQuantity(long productId, int quantity) {
        if (!items.containsKey(productId)) {
            throw new IllegalArgumentException("Product not found in cart");
        }

        if (quantity <= 0) {
            // Remove item if quantity is 0 or negative
            removeProduct(productId);
        } else {
            CartItem item = items.get(productId);
            if (quantity > item.getProduct().getStock()) {
                throw new IllegalArgumentException("Quantity exceeds available stock");
            }
            item.setQuantity(quantity);
        }
    }

    /**
     * Remove a product from the cart
     *
     * @param productId the product ID to remove
     */
    public void removeProduct(long productId) {
        items.remove(productId);
    }

    /**
     * Get a specific cart item
     *
     * @param productId the product ID
     * @return the cart item, or null if not found
     */
    public CartItem getItem(long productId) {
        return items.get(productId);
    }

    /**
     * Get all cart items
     *
     * @return collection of cart items
     */
    public Collection<CartItem> getItems() {
        return items.values();
    }

    /**
     * Get all cart items as a list (sorted by product name)
     *
     * @return list of cart items
     */
    public List<CartItem> getItemsList() {
        List<CartItem> itemsList = new ArrayList<>(items.values());
        itemsList.sort(Comparator.comparing(item -> item.getProduct().getName()));
        return itemsList;
    }

    /**
     * Get total number of items in cart (sum of quantities)
     *
     * @return total item count
     */
    public int getItemCount() {
        return items.values().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    /**
     * Get total number of unique products in cart
     *
     * @return number of unique products
     */
    public int getProductCount() {
        return items.size();
    }

    /**
     * Get total price of all items in cart (in cents)
     *
     * @return total price in cents
     */
    public int getTotalCents() {
        return items.values().stream()
                .mapToInt(CartItem::getTotalCents)
                .sum();
    }

    /**
     * Get total price in euros
     *
     * @return total price in euros
     */
    public double getTotalEuros() {
        return getTotalCents() / 100.0;
    }

    /**
     * Get formatted total price with currency symbol
     *
     * @return formatted total price
     */
    public String getFormattedTotal() {
        return String.format("%.2f €", getTotalEuros());
    }

    /**
     * Check if cart is empty
     *
     * @return true if cart has no items
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Clear all items from cart
     */
    public void clear() {
        items.clear();
    }

    /**
     * Check if a product is in the cart
     *
     * @param productId the product ID
     * @return true if product is in cart
     */
    public boolean containsProduct(long productId) {
        return items.containsKey(productId);
    }

    /**
     * Validate all cart items against current stock
     * Removes items that are out of stock or adjusts quantities that exceed stock
     *
     * @return list of messages about adjustments made
     */
    public List<String> validateStock() {
        List<String> messages = new ArrayList<>();
        List<Long> toRemove = new ArrayList<>();

        for (Map.Entry<Long, CartItem> entry : items.entrySet()) {
            CartItem item = entry.getValue();
            Product product = item.getProduct();

            if (product.getStock() == 0) {
                // Product no longer available
                toRemove.add(entry.getKey());
                messages.add(product.getName() + " n'est plus disponible et a été retiré du panier");
            } else if (item.getQuantity() > product.getStock()) {
                // Quantity exceeds stock - adjust
                int oldQuantity = item.getQuantity();
                item.setQuantity(product.getStock());
                messages.add(product.getName() + " : quantité réduite de " + oldQuantity +
                           " à " + product.getStock() + " (stock disponible)");
            }
        }

        // Remove unavailable items
        for (Long productId : toRemove) {
            items.remove(productId);
        }

        return messages;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "items=" + items.size() +
                ", totalItems=" + getItemCount() +
                ", total=" + getFormattedTotal() +
                '}';
    }
}
