package com.minishop.web.servlet.admin;

import com.minishop.config.AppConstants;
import com.minishop.model.Product;
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
 * Admin product edit servlet
 * Handles product editing
 */
@WebServlet(name = "AdminProductEditServlet", urlPatterns = {AppConstants.SERVLET_ADMIN_PRODUCT_EDIT})
public class AdminProductEditServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AdminProductEditServlet.class.getName());
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        super.init();
        productService = ProductService.getInstance();
    }

    /**
     * Display edit form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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

            // Get product
            Product product = productService.getProductById(productId);
            if (product == null) {
                session.setAttribute(AppConstants.SESSION_ERROR_MESSAGE, "Produit non trouvé");
                response.sendRedirect(request.getContextPath() + AppConstants.SERVLET_ADMIN_PRODUCTS);
                return;
            }

            request.setAttribute("product", product);
            request.setAttribute("pageTitle", "Modifier " + product.getName() + " - Admin");
            request.setAttribute("mode", "edit");
            request.getRequestDispatcher(AppConstants.JSP_ADMIN_PRODUCT_FORM).forward(request, response);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading product for edit", e);
            session.setAttribute(AppConstants.SESSION_ERROR_MESSAGE,
                    "Erreur lors du chargement du produit");
            response.sendRedirect(request.getContextPath() + AppConstants.SERVLET_ADMIN_PRODUCTS);
        }
    }

    /**
     * Process edit form
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

            long productId = Long.parseLong(idParam);

            // Get form parameters
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String priceStr = request.getParameter("price");
            String stockStr = request.getParameter("stock");

            // Validate input
            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Le nom du produit est requis");
                request.setAttribute("mode", "edit");
                doGet(request, response);
                return;
            }

            if (priceStr == null || priceStr.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Le prix est requis");
                request.setAttribute("mode", "edit");
                doGet(request, response);
                return;
            }

            // Parse price (convert euros to cents)
            double priceEuros;
            try {
                priceEuros = Double.parseDouble(priceStr);
                if (priceEuros < 0) {
                    throw new NumberFormatException("Price cannot be negative");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Prix invalide");
                request.setAttribute("mode", "edit");
                doGet(request, response);
                return;
            }

            int priceCents = (int) Math.round(priceEuros * 100);

            // Parse stock
            int stock = 0;
            if (stockStr != null && !stockStr.trim().isEmpty()) {
                try {
                    stock = Integer.parseInt(stockStr);
                    if (stock < 0) {
                        throw new NumberFormatException("Stock cannot be negative");
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Stock invalide");
                    request.setAttribute("mode", "edit");
                    doGet(request, response);
                    return;
                }
            }

            // Update product
            Product product = new Product();
            product.setId(productId);
            product.setName(name.trim());
            product.setDescription(description != null ? description.trim() : "");
            product.setPriceCents(priceCents);
            product.setStock(stock);

            productService.updateProduct(product);

            session.setAttribute(AppConstants.SESSION_SUCCESS_MESSAGE,
                    "Produit modifié avec succès : " + product.getName());
            LOGGER.info("Admin updated product: " + product.getName());

            response.sendRedirect(request.getContextPath() + AppConstants.SERVLET_ADMIN_PRODUCTS);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating product", e);
            session.setAttribute(AppConstants.SESSION_ERROR_MESSAGE,
                    "Erreur lors de la modification du produit");
            response.sendRedirect(request.getContextPath() + AppConstants.SERVLET_ADMIN_PRODUCTS);
        }
    }
}

