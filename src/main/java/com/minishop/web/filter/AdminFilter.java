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
 * Admin filter
 * Protects /admin/* routes - only allows ADMIN role users
 */
@WebFilter(filterName = "AdminFilter", urlPatterns = {"/admin/*"})
public class AdminFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(AdminFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("AdminFilter initialized - protecting /admin/*");
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
            LOGGER.warning("Unauthorized admin access attempt (not logged in) to: " + path);
            session = request.getSession(true);
            session.setAttribute(AppConstants.SESSION_ERROR_MESSAGE,
                    "Vous devez être connecté pour accéder à cette page");
            response.sendRedirect(contextPath + AppConstants.SERVLET_LOGIN);
            return;
        }

        // Check if user is admin
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            // User is logged in but not admin - forbidden
            LOGGER.warning("Forbidden admin access attempt by non-admin user: " + user.getEmail() + " to: " + path);
            session.setAttribute(AppConstants.SESSION_ERROR_MESSAGE,
                    "Accès refusé : vous devez être administrateur");
            response.sendRedirect(contextPath + AppConstants.SERVLET_HOME);
            return;
        }

        // User is admin, continue
        LOGGER.info("Admin access granted to: " + user.getEmail() + " for: " + path);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        LOGGER.info("AdminFilter destroyed");
    }
}

