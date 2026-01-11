package com.minishop.web.servlet.cart;

import com.minishop.service.CartService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Cart merge servlet
 * Merges guest cart from localStorage with session cart
 * Accepts JSON: [{productId: 1, quantity: 2}, ...]
 * Returns JSON: {success: true, message: "...", itemsAdded: 3}
 */
@WebServlet(name = "CartMergeServlet", urlPatterns = {"/cart/merge"})
public class CartMergeServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CartMergeServlet.class.getName());
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = CartService.getInstance();
    }

    /**
     * Merge guest cart with session cart
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set response type to JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();

        try {
            // Check if user is logged in
            if (session == null || session.getAttribute("user") == null) {
                sendErrorResponse(out, "Utilisateur non connecté");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // Read JSON body
            StringBuilder jsonBuffer = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    jsonBuffer.append(line);
                }
            }

            String jsonBody = jsonBuffer.toString().trim();

            if (jsonBody.isEmpty() || jsonBody.equals("[]")) {
                // Empty cart - nothing to merge
                sendSuccessResponse(out, "Aucun article à fusionner", 0);
                return;
            }

            // Parse JSON manually (simple parsing for [{productId:1,quantity:2},...])
            int itemsAdded = parseAndMergeCart(jsonBody, session);

            if (itemsAdded >= 0) {
                String message = itemsAdded > 0
                    ? itemsAdded + " article" + (itemsAdded > 1 ? "s" : "") + " fusionné" + (itemsAdded > 1 ? "s" : "") + " avec votre panier"
                    : "Panier fusionné avec succès";
                sendSuccessResponse(out, message, itemsAdded);
                LOGGER.info("Cart merged successfully: " + itemsAdded + " items");
            } else {
                sendErrorResponse(out, "Erreur lors de la fusion du panier");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error merging cart", e);
            sendErrorResponse(out, "Erreur serveur: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Parse JSON and merge cart items
     * Simple JSON parser for [{productId:1,quantity:2},...]
     */
    private int parseAndMergeCart(String json, HttpSession session) {
        int itemsAdded = 0;

        try {
            // Remove [ ] and split by },{ or },{
            json = json.trim();
            if (json.startsWith("[")) {
                json = json.substring(1);
            }
            if (json.endsWith("]")) {
                json = json.substring(0, json.length() - 1);
            }

            json = json.trim();
            if (json.isEmpty()) {
                return 0;
            }

            // Split items
            String[] items = json.split("\\},\\s*\\{");

            for (String item : items) {
                // Remove { and }
                item = item.replace("{", "").replace("}", "").trim();

                if (item.isEmpty()) {
                    continue;
                }

                // Parse productId and quantity
                long productId = -1;
                int quantity = 0;

                // Split by comma
                String[] fields = item.split(",");
                for (String field : fields) {
                    field = field.trim();
                    String[] keyValue = field.split(":");

                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim().replace("\"", "");
                        String value = keyValue[1].trim().replace("\"", "");

                        if (key.equals("productId")) {
                            try {
                                productId = Long.parseLong(value);
                            } catch (NumberFormatException e) {
                                LOGGER.warning("Invalid productId: " + value);
                            }
                        } else if (key.equals("quantity")) {
                            try {
                                quantity = Integer.parseInt(value);
                            } catch (NumberFormatException e) {
                                LOGGER.warning("Invalid quantity: " + value);
                            }
                        }
                    }
                }

                // Add to cart if valid
                if (productId > 0 && quantity > 0) {
                    boolean success = cartService.addToCart(session, productId, quantity);
                    if (success) {
                        itemsAdded++;
                        LOGGER.info("Added to cart: productId=" + productId + ", quantity=" + quantity);
                    } else {
                        LOGGER.warning("Failed to add to cart: productId=" + productId);
                    }
                }
            }

            return itemsAdded;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error parsing cart JSON", e);
            return -1;
        }
    }

    /**
     * Send success JSON response
     */
    private void sendSuccessResponse(PrintWriter out, String message, int itemsAdded) {
        out.print("{");
        out.print("\"success\": true,");
        out.print("\"message\": \"" + escapeJson(message) + "\",");
        out.print("\"itemsAdded\": " + itemsAdded);
        out.print("}");
        out.flush();
    }

    /**
     * Send error JSON response
     */
    private void sendErrorResponse(PrintWriter out, String message) {
        out.print("{");
        out.print("\"success\": false,");
        out.print("\"message\": \"" + escapeJson(message) + "\"");
        out.print("}");
        out.flush();
    }

    /**
     * Escape string for JSON
     */
    private String escapeJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    /**
     * Redirect GET requests
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/cart");
    }
}
