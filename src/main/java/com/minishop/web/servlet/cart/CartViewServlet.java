package com.minishop.web.servlet.cart;

import com.minishop.config.AppConstants;
import com.minishop.model.Cart;
import com.minishop.service.CartService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Cart view servlet
 * Displays the shopping cart
 */
@WebServlet(name = "CartViewServlet", urlPatterns = {"/cart"})
public class CartViewServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CartViewServlet.class.getName());
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = CartService.getInstance();
    }

    /**
     * Display shopping cart
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Get or create cart
        Cart cart = cartService.getOrCreateCart(session);

        // Validate cart items against current stock
        List<String> validationMessages = cartService.validateCart(session);

        // Set validation messages if any
        if (!validationMessages.isEmpty()) {
            request.setAttribute("validationMessages", validationMessages);
        }

        // Set cart in request for JSP
        request.setAttribute("cart", cart);
        request.setAttribute("pageTitle", "Mon Panier - MiniShop");

        LOGGER.info("Displaying cart with " + cart.getItemCount() + " items");

        // Forward to cart JSP
        request.getRequestDispatcher(AppConstants.JSP_CART).forward(request, response);
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
