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
 * Product list servlet.
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Product> products = productService.getAllProducts();

            request.setAttribute("products", products);
            request.setAttribute("productCount", products.size());
            request.setAttribute("pageTitle", "Catalogue - MiniShop");

            LOGGER.info("Displaying " + products.size() + " products");

            request.getRequestDispatcher(AppConstants.JSP_PRODUCTS).forward(request, response);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error displaying product list", e);
            request.setAttribute("errorMessage",
                "Une erreur est survenue lors du chargement des produits. Veuillez réessayer.");
            request.getRequestDispatcher(AppConstants.JSP_PRODUCTS).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
