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

    // Session Attributes
    public static final String SESSION_USER = "user";
    public static final String SESSION_CART = "cart";
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

    // JSP Paths
    public static final String JSP_HOME = "/WEB-INF/jsp/public/home.jsp";
    public static final String JSP_LOGIN = "/WEB-INF/jsp/auth/login.jsp";
    public static final String JSP_REGISTER = "/WEB-INF/jsp/auth/register.jsp";
    public static final String JSP_PRODUCTS = "/WEB-INF/jsp/product/list.jsp";
    public static final String JSP_PRODUCT_DETAIL = "/WEB-INF/jsp/product/detail.jsp";
    public static final String JSP_CART = "/WEB-INF/jsp/cart/cart.jsp";
    public static final String JSP_CHECKOUT = "/WEB-INF/jsp/cart/checkout.jsp";
    public static final String JSP_ORDER_CONFIRMATION = "/WEB-INF/jsp/cart/confirmation.jsp";
    public static final String JSP_PROFILE = "/WEB-INF/jsp/user/profile.jsp";
    public static final String JSP_ORDERS = "/WEB-INF/jsp/user/orders.jsp";
    public static final String JSP_ERROR_404 = "/WEB-INF/jsp/error/404.jsp";
    public static final String JSP_ERROR_500 = "/WEB-INF/jsp/error/500.jsp";

    // Servlet Mappings
    public static final String SERVLET_HOME = "/home";
    public static final String SERVLET_LOGIN = "/login";
    public static final String SERVLET_LOGOUT = "/logout";
    public static final String SERVLET_REGISTER = "/register";
    public static final String SERVLET_PRODUCTS = "/products";
    public static final String SERVLET_PRODUCT_DETAIL = "/product";
    public static final String SERVLET_CART = "/cart";
    public static final String SERVLET_CHECKOUT = "/checkout";
    public static final String SERVLET_PROFILE = "/profile";
    public static final String SERVLET_ORDERS = "/orders";

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
