/**
 * MiniShop - Cart Merge Manager
 * Automatically merges guest cart from localStorage with session cart after login
 */

(function() {
    'use strict';

    const CART_KEY = 'minishop_cart';

    /**
     * Merge guest cart with session cart
     * Called after login or on cart page load
     */
    function mergeGuestCart(contextPath) {
        // Get guest cart from localStorage
        const guestCart = getGuestCartData();

        if (!guestCart || guestCart.length === 0) {
            console.log('No guest cart to merge');
            return Promise.resolve({ success: true, itemsAdded: 0 });
        }

        console.log('Merging guest cart:', guestCart);

        // Prepare context path
        const basePath = contextPath || '';
        const mergeUrl = basePath + '/cart/merge';

        // Send cart data to server
        return fetch(mergeUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(guestCart),
            credentials: 'same-origin'
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Server returned ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            console.log('Merge response:', data);

            if (data.success) {
                // Clear guest cart from localStorage
                clearGuestCartData();

                // Show success message if items were added
                if (data.itemsAdded > 0 && window.MiniShopGuestCart) {
                    window.MiniShopGuestCart.showNotification(
                        data.message || 'Panier fusionné avec succès',
                        'success'
                    );
                }

                // Reload page to show updated cart
                if (data.itemsAdded > 0 && window.location.pathname.includes('/cart')) {
                    setTimeout(() => window.location.reload(), 1000);
                }
            } else {
                console.error('Merge failed:', data.message);
                if (window.MiniShopGuestCart) {
                    window.MiniShopGuestCart.showNotification(
                        'Erreur lors de la fusion du panier',
                        'danger'
                    );
                }
            }

            return data;
        })
        .catch(error => {
            console.error('Error merging cart:', error);
            if (window.MiniShopGuestCart) {
                window.MiniShopGuestCart.showNotification(
                    'Impossible de fusionner le panier',
                    'warning'
                );
            }
            return { success: false, error: error.message };
        });
    }

    /**
     * Get guest cart data from localStorage
     */
    function getGuestCartData() {
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
     * Clear guest cart from localStorage
     */
    function clearGuestCartData() {
        try {
            localStorage.removeItem(CART_KEY);
            localStorage.removeItem('minishop_cart_count');
            console.log('Guest cart cleared from localStorage');

            // Update badge to 0
            if (window.MiniShopGuestCart) {
                const badge = document.querySelector('.cart-count-badge');
                if (badge) {
                    badge.style.display = 'none';
                }
            }
        } catch (e) {
            console.error('Failed to clear guest cart:', e);
        }
    }

    /**
     * Check if user is logged in (client-side detection)
     */
    function isUserLoggedIn() {
        // Check for user-specific elements in the DOM
        const userMenu = document.getElementById('userDropdown');
        return userMenu !== null;
    }

    /**
     * Auto-merge cart on page load if user is logged in
     */
    function autoMergeOnLoad() {
        if (isUserLoggedIn()) {
            const guestCart = getGuestCartData();
            if (guestCart && guestCart.length > 0) {
                console.log('Auto-merging guest cart on page load');
                // Get context path from page
                const contextPath = document.querySelector('meta[name="context-path"]')?.content || '';
                mergeGuestCart(contextPath);
            }
        }
    }

    // Initialize on DOM ready
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', function() {
            // Don't auto-merge on login page to avoid race condition
            if (!window.location.pathname.includes('/login')) {
                setTimeout(autoMergeOnLoad, 100);
            }
        });
    } else {
        if (!window.location.pathname.includes('/login')) {
            setTimeout(autoMergeOnLoad, 100);
        }
    }

    // Export function to window for external access
    window.MiniShopCartMerge = {
        merge: mergeGuestCart,
        clear: clearGuestCartData,
        autoMerge: autoMergeOnLoad
    };

})();
