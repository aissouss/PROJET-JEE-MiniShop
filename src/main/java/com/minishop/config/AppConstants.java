package com.minishop.config;

/**
 * Application-wide constants
 */
public final class AppConstants {

    // Database Configuration
    public static final String DB_URL = "jdbc:mysql://localhost:3306/minishop";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "";
    public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    // Session Attributes (as per specification)
    public static final String AUTH_USER = "AUTH_USER";
    public static final String CART = "CART";
    public static final String SESSION_CART_COUNT = "cartCount";

    // Session Messages
    public static final String SESSION_SUCCESS_MESSAGE = "successMessage";
    public static final String SESSION_ERROR_MESSAGE = "errorMessage";
    public static final String SESSION_INFO_MESSAGE = "infoMessage";

    // User Roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";

    // Pagination
    public static final int DEFAULT_PAGE_SIZE = 12;
    public static final int ADMIN_PAGE_SIZE = 20;

    // JSP Paths (as per specification)
    public static final String JSP_HOME = "/WEB-INF/jsp/public/home.jsp";
    public static final String JSP_LOGIN = "/WEB-INF/jsp/public/login.jsp";
    public static final String JSP_REGISTER = "/WEB-INF/jsp/public/register.jsp";
    public static final String JSP_PRODUCTS = "/WEB-INF/jsp/public/products.jsp";
    public static final String JSP_PRODUCT_DETAIL = "/WEB-INF/jsp/public/product-detail.jsp";
    public static final String JSP_CART = "/WEB-INF/jsp/app/cart.jsp";
    public static final String JSP_CHECKOUT = "/WEB-INF/jsp/app/checkout.jsp";
    public static final String JSP_ORDER_CONFIRMATION = "/WEB-INF/jsp/app/confirmation.jsp";
    public static final String JSP_PROFILE = "/WEB-INF/jsp/app/profile.jsp";
    public static final String JSP_ORDERS = "/WEB-INF/jsp/app/orders.jsp";
    public static final String JSP_ERROR_404 = "/WEB-INF/jsp/error/404.jsp";
    public static final String JSP_ERROR_500 = "/WEB-INF/jsp/error/500.jsp";

    // Servlet Mappings (protected routes use /app/* prefix)
    public static final String SERVLET_HOME = "/home";
    public static final String SERVLET_LOGIN = "/login";
    public static final String SERVLET_LOGOUT = "/logout";
    public static final String SERVLET_REGISTER = "/register";
    public static final String SERVLET_PRODUCTS = "/products";
    public static final String SERVLET_PRODUCT_DETAIL = "/product";
    public static final String SERVLET_CART = "/app/cart";
    public static final String SERVLET_CART_ADD = "/app/cart/add";
    public static final String SERVLET_CART_REMOVE = "/app/cart/remove";
    public static final String SERVLET_CART_MERGE = "/app/cart/merge";
    public static final String SERVLET_CHECKOUT = "/app/checkout";
    public static final String SERVLET_PROFILE = "/app/profile";
    public static final String SERVLET_ORDERS = "/app/orders";

    // Validation
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_PASSWORD_LENGTH = 100;
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 100;

    // Price formatting
    public static final String CURRENCY_SYMBOL = "€";
    public static final String PRICE_FORMAT = "%.2f";

    // Date format
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATETIME_FORMAT = "dd/MM/yyyy HH:mm";

    // Private constructor to prevent instantiation
    private AppConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
