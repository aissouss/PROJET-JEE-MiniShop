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
 * Cart add servlet
 * Adds a product to the shopping cart
 */
@WebServlet(name = "CartAddServlet", urlPatterns = {"/cart/add"})
public class CartAddServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CartAddServlet.class.getName());
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = CartService.getInstance();
    }

    /**
     * Add product to cart
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        try {
            // Get parameters
            String productIdParam = request.getParameter("productId");
            String quantityParam = request.getParameter("quantity");

            // Validate parameters
            if (productIdParam == null || productIdParam.trim().isEmpty()) {
                session.setAttribute(AppConstants.SESSION_ERROR_MESSAGE,
                    "Identifiant du produit manquant");
                response.sendRedirect(request.getContextPath() + AppConstants.SERVLET_PRODUCTS);
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
                response.sendRedirect(request.getContextPath() + AppConstants.SERVLET_PRODUCTS);
                return;
            }

            // Parse quantity (default to 1 if not provided)
            int quantity = 1;
            if (quantityParam != null && !quantityParam.trim().isEmpty()) {
                try {
                    quantity = Integer.parseInt(quantityParam);
                } catch (NumberFormatException e) {
                    LOGGER.warning("Invalid quantity format: " + quantityParam);
                    session.setAttribute(AppConstants.SESSION_ERROR_MESSAGE,
                        "Quantité invalide");
                    response.sendRedirect(request.getContextPath() +
                        AppConstants.SERVLET_PRODUCT_DETAIL + "?id=" + productId);
                    return;
                }
            }

            // Validate quantity
            if (quantity <= 0) {
                session.setAttribute(AppConstants.SESSION_ERROR_MESSAGE,
                    "La quantité doit être supérieure à zéro");
                response.sendRedirect(request.getContextPath() +
                    AppConstants.SERVLET_PRODUCT_DETAIL + "?id=" + productId);
                return;
            }

            // Add to cart
            boolean success = cartService.addToCart(session, productId, quantity);

            if (success) {
                session.setAttribute(AppConstants.SESSION_SUCCESS_MESSAGE,
                    "Produit ajouté au panier avec succès");
                LOGGER.info("Product added to cart: " + productId + " x" + quantity);
            } else {
                session.setAttribute(AppConstants.SESSION_ERROR_MESSAGE,
                    "Impossible d'ajouter le produit au panier. Vérifiez la disponibilité.");
                LOGGER.warning("Failed to add product to cart: " + productId);
            }

            // Redirect to cart page
            response.sendRedirect(request.getContextPath() + "/cart");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding product to cart", e);
            session.setAttribute(AppConstants.SESSION_ERROR_MESSAGE,
                "Une erreur est survenue lors de l'ajout au panier");
            response.sendRedirect(request.getContextPath() + AppConstants.SERVLET_PRODUCTS);
        }
    }

    /**
     * Redirect GET requests to products page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + AppConstants.SERVLET_PRODUCTS);
    }
}
