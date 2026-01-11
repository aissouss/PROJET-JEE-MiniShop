package com.minishop.service;

import com.minishop.config.AppConstants;
import com.minishop.model.Cart;
import com.minishop.model.Product;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.logging.Logger;

/**
 * Cart service
 * Handles shopping cart operations in session
 */
public class CartService {

    private static final Logger LOGGER = Logger.getLogger(CartService.class.getName());
    private static CartService instance;

    private final ProductService productService;

    private CartService() {
        this.productService = ProductService.getInstance();
    }

    /**
     * Get singleton instance of CartService
     */
    public static synchronized CartService getInstance() {
        if (instance == null) {
            instance = new CartService();
        }
        return instance;
    }

    /**
     * Get cart from session, or create new one if it doesn't exist
     *
     * @param session the HTTP session
     * @return the cart
     */
    public Cart getOrCreateCart(HttpSession session) {
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null");
        }

        Cart cart = (Cart) session.getAttribute(AppConstants.CART);
        if (cart == null) {
            cart = new Cart();
            session.setAttribute(AppConstants.CART, cart);
            LOGGER.info("Created new cart in session");
        }

        // Update cart count in session
        updateCartCount(session, cart);

        return cart;
    }

    /**
     * Add a product to the cart
     *
     * @param session   the HTTP session
     * @param productId the product ID
     * @param quantity  the quantity to add
     * @return true if successful, false otherwise
     */
    public void addToCart(HttpSession session, long productId, int quantity) {
        // Validate inputs
        if (session == null) {
            LOGGER.warning("Cannot add to cart: session is null");
            throw new IllegalArgumentException("Session required");
        }

        if (quantity <= 0) {
            LOGGER.warning("Cannot add to cart: invalid quantity " + quantity);
            throw new IllegalArgumentException("Invalid quantity");
        }

        // Get product
        Product product = productService.getProductById(productId);
        if (product == null) {
            LOGGER.warning("Cannot add to cart: product not found " + productId);
            throw new IllegalArgumentException("Product not found");
        }

        // Check stock
        if (quantity > product.getStock()) {
            LOGGER.warning("Cannot add to cart: insufficient stock for product " + productId +
                         " (requested: " + quantity + ", available: " + product.getStock() + ")");
            throw new IllegalArgumentException("Insufficient stock");
        }

        // Get or create cart
        Cart cart = getOrCreateCart(session);

        // Add product to cart
        cart.addProduct(product, quantity);

        // Update cart count in session
        updateCartCount(session, cart);

        LOGGER.info("Added to cart: " + product.getName() + " x" + quantity);
    }

    /**
     * Update quantity of a product in the cart
     *
     * @param session   the HTTP session
     * @param productId the product ID
     * @param quantity  the new quantity
     * @return true if successful, false otherwise
     */
    public void updateQuantity(HttpSession session, long productId, int quantity) {
        if (session == null) {
            throw new IllegalArgumentException("Session required");
        }

        Cart cart = getOrCreateCart(session);

        if (quantity <= 0) {
            // Remove item if quantity is 0 or negative
            removeFromCart(session, productId);
            return;
        }

        cart.updateQuantity(productId, quantity);

        // Update cart count in session
        updateCartCount(session, cart);

        LOGGER.info("Updated cart quantity for product " + productId + ": " + quantity);
    }

    /**
     * Remove a product from the cart
     *
     * @param session   the HTTP session
     * @param productId the product ID
     * @return true if successful, false otherwise
     */
    public void removeFromCart(HttpSession session, long productId) {
        if (session == null) {
            throw new IllegalArgumentException("Session required");
        }

        Cart cart = getOrCreateCart(session);
        cart.removeProduct(productId);

        // Update cart count in session
        updateCartCount(session, cart);

        LOGGER.info("Removed from cart: product " + productId);
    }

    /**
     * Clear all items from the cart
     *
     * @param session the HTTP session
     */
    public void clearCart(HttpSession session) {
        if (session == null) {
            return;
        }

        Cart cart = getOrCreateCart(session);
        cart.clear();

        // Update cart count in session
        updateCartCount(session, cart);

        LOGGER.info("Cart cleared");
    }

    /**
     * Validate cart items against current stock
     * Removes out-of-stock items and adjusts quantities if needed
     *
     * @param session the HTTP session
     * @return list of messages about adjustments made
     */
    public List<String> validateCart(HttpSession session) {
        if (session == null) {
            return List.of();
        }

        Cart cart = getOrCreateCart(session);
        List<String> messages = cart.validateStock();

        // Update cart count in session
        updateCartCount(session, cart);

        if (!messages.isEmpty()) {
            LOGGER.info("Cart validated with " + messages.size() + " adjustments");
        }

        return messages;
    }

    /**
     * Update cart count in session attribute
     * Used for displaying badge in navigation
     *
     * @param session the HTTP session
     * @param cart    the cart
     */
    private void updateCartCount(HttpSession session, Cart cart) {
        if (session != null && cart != null) {
            session.setAttribute(AppConstants.SESSION_CART_COUNT, cart.getItemCount());
        }
    }

    /**
     * Get cart item count from session
     *
     * @param session the HTTP session
     * @return item count, or 0 if cart is empty or doesn't exist
     */
    public int getCartItemCount(HttpSession session) {
        if (session == null) {
            return 0;
        }

        Integer count = (Integer) session.getAttribute(AppConstants.SESSION_CART_COUNT);
        return count != null ? count : 0;
    }
}
