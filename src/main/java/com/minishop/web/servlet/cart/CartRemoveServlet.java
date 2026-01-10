package com.minishop.web.servlet.cart;

import com.minishop.config.AppConstants;
import com.minishop.service.CartService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Cart remove servlet
 * Removes a product from the shopping cart
 */
@WebServlet(name = "CartRemoveServlet", urlPatterns = {"/cart/remove"})
public class CartRemoveServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CartRemoveServlet.class.getName());
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = CartService.getInstance();
    }

    /**
     * Remove product from cart
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        try {
            // Get product ID parameter
            String productIdParam = request.getParameter("productId");

            // Validate parameter
            if (productIdParam == null || productIdParam.trim().isEmpty()) {
                session.setAttribute(AppConstants.SESSION_ERROR_MESSAGE,
                    "Identifiant du produit manquant");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            // Parse product ID
            long productId;
            try {
                productId = Long.parseLong(productIdParam);
            } catch (NumberFormatException e) {
                LOGGER.warning("Invalid product ID format: " + productIdParam);
                session.setAttribute(AppConstants.SESSION_ERROR_MESSAGE,
                    "Identifiant du produit invalide");
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            // Remove from cart
            boolean success = cartService.removeFromCart(session, productId);

            if (success) {
                session.setAttribute(AppConstants.SESSION_SUCCESS_MESSAGE,
                    "Produit retiré du panier");
                LOGGER.info("Product removed from cart: " + productId);
            } else {
                session.setAttribute(AppConstants.SESSION_ERROR_MESSAGE,
                    "Impossible de retirer le produit du panier");
                LOGGER.warning("Failed to remove product from cart: " + productId);
            }

            // Redirect to cart page
            response.sendRedirect(request.getContextPath() + "/cart");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error removing product from cart", e);
            session.setAttribute(AppConstants.SESSION_ERROR_MESSAGE,
                "Une erreur est survenue lors de la suppression");
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }

    /**
     * Redirect GET requests to cart page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/cart");
    }
}
