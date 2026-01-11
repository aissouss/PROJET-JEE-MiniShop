/**
 * MiniShop - Guest Cart Manager (LocalStorage)
 * Handles shopping cart for non-authenticated users
 */

(function() {
    'use strict';

    const CART_KEY = 'minishop_cart';
    const CART_COUNT_KEY = 'minishop_cart_count';

    /**
     * Get guest cart from localStorage
     * Returns array of {productId, quantity, productName, price}
     */
    function getGuestCart() {
        try {
            const cartJson = localStorage.getItem(CART_KEY);
            if (!cartJson) {
                return [];
            }
            return JSON.parse(cartJson);
        } catch (e) {
            console.error('Failed to parse guest cart:', e);
            return [];
        }
    }

    /**
     * Save guest cart to localStorage
     */
    function saveGuestCart(cart) {
        try {
            localStorage.setItem(CART_KEY, JSON.stringify(cart));
            updateGuestCartCount(cart);
            console.log('Guest cart saved:', cart);
            return true;
        } catch (e) {
            console.error('Failed to save guest cart:', e);
            return false;
        }
    }

    /**
     * Update cart count in localStorage and badge
     */
    function updateGuestCartCount(cart) {
        const count = cart.reduce((total, item) => total + item.quantity, 0);
        try {
            localStorage.setItem(CART_COUNT_KEY, count.toString());
            updateCartBadge(count);
        } catch (e) {
            console.error('Failed to update cart count:', e);
        }
    }

    /**
     * Update cart count badge in navigation
     */
    function updateCartBadge(count) {
        const badge = document.querySelector('.cart-count-badge');
        if (badge) {
            if (count > 0) {
                badge.textContent = count;
                badge.style.display = 'inline';
            } else {
                badge.style.display = 'none';
            }
        }
    }

    /**
     * Add product to guest cart
     */
    function addToGuestCart(productId, quantity, productName, price) {
        if (!productId || quantity <= 0) {
            console.error('Invalid product or quantity');
            return false;
        }

        let cart = getGuestCart();

        // Check if product already in cart
        const existingItem = cart.find(item => item.productId === productId);

        if (existingItem) {
            // Increment quantity
            existingItem.quantity += quantity;
        } else {
            // Add new item
            cart.push({
                productId: parseInt(productId),
                quantity: parseInt(quantity),
                productName: productName || 'Produit',
                price: price || 0
            });
        }

        return saveGuestCart(cart);
    }

    /**
     * Remove product from guest cart
     */
    function removeFromGuestCart(productId) {
        let cart = getGuestCart();
        cart = cart.filter(item => item.productId !== parseInt(productId));
        return saveGuestCart(cart);
    }

    /**
     * Update product quantity in guest cart
     */
    function updateGuestCartQuantity(productId, quantity) {
        let cart = getGuestCart();
        const item = cart.find(item => item.productId === parseInt(productId));

        if (item) {
            if (quantity > 0) {
                item.quantity = quantity;
            } else {
                // Remove if quantity is 0
                cart = cart.filter(item => item.productId !== parseInt(productId));
            }
            return saveGuestCart(cart);
        }

        return false;
    }

    /**
     * Clear guest cart
     */
    function clearGuestCart() {
        try {
            localStorage.removeItem(CART_KEY);
            localStorage.removeItem(CART_COUNT_KEY);
            updateCartBadge(0);
            console.log('Guest cart cleared');
            return true;
        } catch (e) {
            console.error('Failed to clear guest cart:', e);
            return false;
        }
    }

    /**
     * Get guest cart item count
     */
    function getGuestCartCount() {
        const cart = getGuestCart();
        return cart.reduce((total, item) => total + item.quantity, 0);
    }

    /**
     * Show notification message
     */
    function showNotification(message, type = 'success') {
        // Create notification element
        const notification = document.createElement('div');
        notification.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
        notification.style.cssText = 'top: 80px; right: 20px; z-index: 9999; min-width: 300px;';
        notification.innerHTML = `
            <i class="bi bi-${type === 'success' ? 'check-circle' : 'exclamation-triangle'} me-2"></i>
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;

        document.body.appendChild(notification);

        // Auto-dismiss after 3 seconds
        setTimeout(() => {
            notification.classList.remove('show');
            setTimeout(() => notification.remove(), 150);
        }, 3000);
    }

    /**
     * Initialize guest cart on page load
     */
    function initGuestCart() {
        // Update cart badge on page load
        const count = getGuestCartCount();
        updateCartBadge(count);

        console.log('Guest cart initialized with', count, 'items');
    }

    // Initialize on DOM ready
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initGuestCart);
    } else {
        initGuestCart();
    }

    // Export functions to window for external access
    window.MiniShopGuestCart = {
        get: getGuestCart,
        add: addToGuestCart,
        remove: removeFromGuestCart,
        update: updateGuestCartQuantity,
        clear: clearGuestCart,
        getCount: getGuestCartCount,
        showNotification: showNotification
    };

})();
