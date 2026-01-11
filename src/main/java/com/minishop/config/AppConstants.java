package com.minishop.config;

/**
 * Application-wide constants
 */
public final class AppConstants {

    // Database Configuration
    public static final String DB_URL = "jdbc:mysql://localhost:3306/minishop";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "";
    // Session Attributes (as per specification)
    public static final String AUTH_USER = "AUTH_USER";
    public static final String CART = "CART";
    public static final String SESSION_CART_COUNT = "cartCount";

    // Session Messages
    public static final String SESSION_SUCCESS_MESSAGE = "successMessage";
    public static final String SESSION_ERROR_MESSAGE = "errorMessage";
    public static final String SESSION_INFO_MESSAGE = "infoMessage";

    // JSP Paths (as per specification)
    public static final String JSP_HOME = "/WEB-INF/jsp/public/home.jsp";
    public static final String JSP_LOGIN = "/WEB-INF/jsp/public/login.jsp";
    public static final String JSP_PRODUCTS = "/WEB-INF/jsp/public/products.jsp";
    public static final String JSP_PRODUCT_DETAIL = "/WEB-INF/jsp/public/product-detail.jsp";
    public static final String JSP_CART = "/WEB-INF/jsp/app/cart.jsp";
    public static final String JSP_ERROR_404 = "/WEB-INF/jsp/error/404.jsp";
    public static final String JSP_ERROR_500 = "/WEB-INF/jsp/error/500.jsp";

    // Servlet Mappings (protected routes use /app/* prefix)
    public static final String SERVLET_HOME = "/home";
    public static final String SERVLET_LOGIN = "/login";
    public static final String SERVLET_LOGOUT = "/logout";
    public static final String SERVLET_PRODUCTS = "/products";
    public static final String SERVLET_PRODUCT_DETAIL = "/product";
    public static final String SERVLET_CART = "/app/cart";
    public static final String SERVLET_CART_ADD = "/app/cart/add";
    public static final String SERVLET_CART_REMOVE = "/app/cart/remove";
    public static final String SERVLET_CART_MERGE = "/app/cart/merge";
    // Private constructor to prevent instantiation
    private AppConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
