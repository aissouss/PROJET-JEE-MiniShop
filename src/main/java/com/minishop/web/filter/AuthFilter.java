package com.minishop.web.filter;

import com.minishop.config.AppConstants;
import com.minishop.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Authentication filter
 * Protects /app/* routes - redirects to login if user is not authenticated
 * As per specification: protects /app/* and /admin/*
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/app/*", "/admin/*"})
public class AuthFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(AuthFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("AuthFilter initialized - protecting /app/* and /admin/*");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                        FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = requestURI.substring(contextPath.length());

        // Get session and check for authenticated user
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute(AppConstants.AUTH_USER) : null;

        // Check if user is authenticated
        if (session == null || user == null) {
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

        // Check admin access for /admin/* routes
        if (path.startsWith("/admin/") && !user.isAdmin()) {
            LOGGER.warning("Non-admin user attempted to access admin page: " + user.getEmail() + " -> " + path);

            session.setAttribute(AppConstants.SESSION_ERROR_MESSAGE,
                    "Accès refusé. Vous devez être administrateur pour accéder à cette page.");

            response.sendRedirect(contextPath + AppConstants.SERVLET_HOME);
            return;
        }

        // User is authenticated, continue
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        LOGGER.info("AuthFilter destroyed");
    }
}
