package com.minishop.web.servlet.admin;

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
 * Admin product list servlet
 * Displays all products with edit/delete options
 */
@WebServlet(name = "AdminProductListServlet", urlPatterns = {AppConstants.SERVLET_ADMIN_PRODUCTS})
public class AdminProductListServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AdminProductListServlet.class.getName());
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
            request.setAttribute("pageTitle", "Gestion Produits - Admin");

            LOGGER.info("Admin viewing " + products.size() + " products");

            request.getRequestDispatcher(AppConstants.JSP_ADMIN_PRODUCTS).forward(request, response);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error displaying admin product list", e);
            request.getSession().setAttribute(AppConstants.SESSION_ERROR_MESSAGE,
                "Une erreur est survenue lors du chargement des produits");
            response.sendRedirect(request.getContextPath() + AppConstants.SERVLET_HOME);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

