package com.minishop.web.filter;

import com.minishop.config.AppConstants;
import com.minishop.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Authentication filter
 * Protects secured pages and ensures user is logged in
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(AuthFilter.class.getName());

    // URLs that don't require authentication
    private static final List<String> PUBLIC_URLS = Arrays.asList(
            AppConstants.SERVLET_HOME,
            AppConstants.SERVLET_LOGIN,
            AppConstants.SERVLET_REGISTER,
            AppConstants.SERVLET_PRODUCTS,
            AppConstants.SERVLET_PRODUCT_DETAIL,
            "/assets/",
            "/favicon.ico"
    );

    // URLs that require authentication
    private static final List<String> PROTECTED_URLS = Arrays.asList(
            AppConstants.SERVLET_CART,
            AppConstants.SERVLET_CHECKOUT,
            AppConstants.SERVLET_PROFILE,
            AppConstants.SERVLET_ORDERS
    );

    // URLs that require admin role
    private static final List<String> ADMIN_URLS = Arrays.asList(
            "/admin/"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("AuthFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                        FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = requestURI.substring(contextPath.length());

        // Allow public resources
        if (isPublicResource(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Check if path requires authentication
        if (requiresAuthentication(path)) {
            HttpSession session = request.getSession(false);
            User user = (session != null) ? (User) session.getAttribute(AppConstants.SESSION_USER) : null;

            if (user == null) {
                // User not logged in - redirect to login page
                LOGGER.info("Unauthorized access attempt to: " + path);

                // Store original URL for redirect after login
                String redirectUrl = path;
                String queryString = request.getQueryString();
                if (queryString != null && !queryString.isEmpty()) {
                    redirectUrl += "?" + queryString;
                }

                response.sendRedirect(contextPath + AppConstants.SERVLET_LOGIN + "?redirect=" + redirectUrl);
                return;
            }

            // Check admin access
            if (requiresAdminRole(path) && !user.isAdmin()) {
                LOGGER.warning("Non-admin user attempted to access admin page: " + user.getEmail() + " -> " + path);

                session.setAttribute(AppConstants.SESSION_ERROR_MESSAGE,
                        "Accès refusé. Vous devez être administrateur pour accéder à cette page.");

                response.sendRedirect(contextPath + AppConstants.SERVLET_HOME);
                return;
            }

            // User is authenticated, continue
            chain.doFilter(request, response);
        } else {
            // Path doesn't require authentication
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        LOGGER.info("AuthFilter destroyed");
    }

    /**
     * Check if the resource is public (doesn't require authentication)
     */
    private boolean isPublicResource(String path) {
        // Check if path starts with any public URL
        for (String publicUrl : PUBLIC_URLS) {
            if (path.equals(publicUrl) || path.startsWith(publicUrl) || path.equals("/")) {
                return true;
            }
        }

        // Allow static resources
        if (path.startsWith("/assets/") ||
            path.startsWith("/css/") ||
            path.startsWith("/js/") ||
            path.startsWith("/images/") ||
            path.endsWith(".css") ||
            path.endsWith(".js") ||
            path.endsWith(".png") ||
            path.endsWith(".jpg") ||
            path.endsWith(".jpeg") ||
            path.endsWith(".gif") ||
            path.endsWith(".ico") ||
            path.endsWith(".svg") ||
            path.endsWith(".woff") ||
            path.endsWith(".woff2") ||
            path.endsWith(".ttf")) {
            return true;
        }

        return false;
    }

    /**
     * Check if the path requires authentication
     */
    private boolean requiresAuthentication(String path) {
        for (String protectedUrl : PROTECTED_URLS) {
            if (path.equals(protectedUrl) || path.startsWith(protectedUrl)) {
                return true;
            }
        }

        for (String adminUrl : ADMIN_URLS) {
            if (path.startsWith(adminUrl)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if the path requires admin role
     */
    private boolean requiresAdminRole(String path) {
        for (String adminUrl : ADMIN_URLS) {
            if (path.startsWith(adminUrl)) {
                return true;
            }
        }
        return false;
    }
}
