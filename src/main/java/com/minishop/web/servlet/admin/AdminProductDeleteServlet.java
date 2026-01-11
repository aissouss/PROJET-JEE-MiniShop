package com.minishop.web.servlet.admin;

import com.minishop.config.AppConstants;
import com.minishop.service.ProductService;
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
 * Admin product delete servlet
 * Handles product deletion
 */
@WebServlet(name = "AdminProductDeleteServlet", urlPatterns = {AppConstants.SERVLET_ADMIN_PRODUCT_DELETE})
public class AdminProductDeleteServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AdminProductDeleteServlet.class.getName());
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        super.init();
        productService = ProductService.getInstance();
    }

    /**
     * Handle product deletion (POST only for safety)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        try {
            // Get product ID
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                session.setAttribute(AppConstants.SESSION_ERROR_MESSAGE, "ID produit manquant");
                response.sendRedirect(request.getContextPath() + AppConstants.SERVLET_ADMIN_PRODUCTS);
                return;
            }

            long productId;
            try {
                productId = Long.parseLong(idParam);
            } catch (NumberFormatException e) {
                session.setAttribute(AppConstants.SESSION_ERROR_MESSAGE, "ID produit invalide");
                response.sendRedirect(request.getContextPath() + AppConstants.SERVLET_ADMIN_PRODUCTS);
                return;
            }

            // Delete product
            productService.deleteProduct(productId);

            session.setAttribute(AppConstants.SESSION_SUCCESS_MESSAGE,
                    "Produit supprimé avec succès");
            LOGGER.info("Admin deleted product ID: " + productId);

            response.sendRedirect(request.getContextPath() + AppConstants.SERVLET_ADMIN_PRODUCTS);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting product", e);
            session.setAttribute(AppConstants.SESSION_ERROR_MESSAGE,
                    "Erreur lors de la suppression du produit");
            response.sendRedirect(request.getContextPath() + AppConstants.SERVLET_ADMIN_PRODUCTS);
        }
    }

    /**
     * Redirect GET requests to admin products list
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + AppConstants.SERVLET_ADMIN_PRODUCTS);
    }
}

