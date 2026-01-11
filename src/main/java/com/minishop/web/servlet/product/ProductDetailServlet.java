package com.minishop.web.servlet.product;

import com.minishop.config.AppConstants;
import com.minishop.model.Product;
import com.minishop.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Product detail servlet
 * Displays detailed information about a specific product
 */
@WebServlet(name = "ProductDetailServlet", urlPatterns = {AppConstants.SERVLET_PRODUCT_DETAIL})
public class ProductDetailServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ProductDetailServlet.class.getName());
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        super.init();
        productService = ProductService.getInstance();
    }

    /**
     * Display product details
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Get product ID from parameter
            String idParam = request.getParameter("id");

            if (idParam == null || idParam.trim().isEmpty()) {
                LOGGER.warning("Product ID parameter is missing");
                response.sendRedirect(request.getContextPath() + AppConstants.SERVLET_PRODUCTS);
                return;
            }

            // Parse product ID
            long productId;
            try {
                productId = Long.parseLong(idParam);
            } catch (NumberFormatException e) {
                LOGGER.warning("Invalid product ID format: " + idParam);
                response.sendRedirect(request.getContextPath() + AppConstants.SERVLET_PRODUCTS);
                return;
            }

            // Get product from service
            Product product = productService.getProductById(productId);

            if (product == null) {
                LOGGER.warning("Product not found: " + productId);
                request.getSession().setAttribute(AppConstants.SESSION_ERROR_MESSAGE,
                    "Le produit demandé n'existe pas.");
                response.sendRedirect(request.getContextPath() + AppConstants.SERVLET_PRODUCTS);
                return;
            }

            // Set attributes for JSP
            request.setAttribute("product", product);
            request.setAttribute("pageTitle", product.getName() + " - MiniShop");

            LOGGER.info("Displaying product: " + product.getName() + " (ID: " + productId + ")");

            // Forward to product detail JSP
            request.getRequestDispatcher(AppConstants.JSP_PRODUCT_DETAIL).forward(request, response);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error displaying product details", e);
            request.getSession().setAttribute(AppConstants.SESSION_ERROR_MESSAGE,
                "Une erreur est survenue lors du chargement du produit.");
            response.sendRedirect(request.getContextPath() + AppConstants.SERVLET_PRODUCTS);
        }
    }

    /**
     * Handle POST requests - redirect to GET
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
