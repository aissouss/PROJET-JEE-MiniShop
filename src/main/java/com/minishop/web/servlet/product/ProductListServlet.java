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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Product list servlet
 * Displays all products in a catalog view
 */
@WebServlet(name = "ProductListServlet", urlPatterns = {AppConstants.SERVLET_PRODUCTS})
public class ProductListServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ProductListServlet.class.getName());
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        super.init();
        productService = ProductService.getInstance();
    }

    /**
     * Display product list
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Get query parameters
            String category = request.getParameter("category");
            String searchTerm = request.getParameter("search");

            List<Product> products;

            // Filter products based on parameters
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                // Search products
                products = productService.searchProducts(searchTerm);
                request.setAttribute("searchTerm", searchTerm);
                request.setAttribute("pageSubtitle", "Résultats pour \"" + searchTerm + "\"");
            } else if (category != null && !category.trim().isEmpty()) {
                // Filter by category
                products = productService.getProductsByCategory(category);
                request.setAttribute("selectedCategory", category);
                request.setAttribute("pageSubtitle", "Catégorie: " + category);
            } else {
                // Get all active products
                products = productService.getAllActiveProducts();
                request.setAttribute("pageSubtitle", "Tous nos produits");
            }

            // Get all categories for filter
            List<String> categories = productService.getAllCategories();

            // Set attributes for JSP
            request.setAttribute("products", products);
            request.setAttribute("categories", categories);
            request.setAttribute("productCount", products.size());

            // Set page title
            request.setAttribute("pageTitle", "Catalogue - MiniShop");

            LOGGER.info("Displaying " + products.size() + " products");

            // Forward to products JSP
            request.getRequestDispatcher(AppConstants.JSP_PRODUCTS).forward(request, response);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error displaying product list", e);
            request.setAttribute("errorMessage",
                "Une erreur est survenue lors du chargement des produits. Veuillez réessayer.");
            request.getRequestDispatcher(AppConstants.JSP_PRODUCTS).forward(request, response);
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
