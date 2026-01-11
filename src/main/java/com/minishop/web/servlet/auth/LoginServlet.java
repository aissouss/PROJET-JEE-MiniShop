package com.minishop.web.servlet.auth;

import com.minishop.config.AppConstants;
import com.minishop.model.User;
import com.minishop.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Login servlet
 * Handles user authentication - displays login form and processes login
 */
@WebServlet(name = "LoginServlet", urlPatterns = {AppConstants.SERVLET_LOGIN})
public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());
    private AuthService authService;

    @Override
    public void init() throws ServletException {
        super.init();
        authService = AuthService.getInstance();
    }

    /**
     * Display login form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // If user is already logged in, redirect to home
        if (session != null && session.getAttribute(AppConstants.AUTH_USER) != null) {
            response.sendRedirect(request.getContextPath() + AppConstants.SERVLET_HOME);
            return;
        }

        // Get redirect URL if any (for redirecting after successful login)
        String redirectUrl = request.getParameter("redirect");
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            request.setAttribute("redirectUrl", redirectUrl);
        }

        // Forward to login page
        request.getRequestDispatcher(AppConstants.JSP_LOGIN).forward(request, response);
    }

    /**
     * Process login form submission
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get form parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String redirectUrl = request.getParameter("redirectUrl");
        String rememberMe = request.getParameter("rememberMe");

        // Validate input
        if (email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {

            request.setAttribute("errorMessage", "Email et mot de passe sont requis");
            request.setAttribute("email", email);
            request.getRequestDispatcher(AppConstants.JSP_LOGIN).forward(request, response);
            return;
        }

        // Attempt authentication
        User user = authService.login(email, password);

        if (user == null) {
            LOGGER.warning("Failed login attempt for email: " + email);

            request.setAttribute("errorMessage", "Email ou mot de passe incorrect");
            request.setAttribute("email", email);
            request.getRequestDispatcher(AppConstants.JSP_LOGIN).forward(request, response);
            return;
        }

        // Create session and store user
        HttpSession session = request.getSession(true);
        session.setAttribute(AppConstants.AUTH_USER, user);

        // Set session timeout based on "remember me"
        if ("on".equals(rememberMe)) {
            session.setMaxInactiveInterval(30 * 24 * 60 * 60); // 30 days
        } else {
            session.setMaxInactiveInterval(30 * 60); // 30 minutes
        }

        // Set success message
        session.setAttribute(AppConstants.SESSION_SUCCESS_MESSAGE,
                "Bienvenue, " + user.getFullName() + " !");

        LOGGER.info("User logged in successfully: " + user.getEmail());

        // Redirect to original URL or home page
        String targetUrl;
        if (redirectUrl != null && !redirectUrl.isEmpty() && isValidRedirectUrl(redirectUrl)) {
            targetUrl = request.getContextPath() + redirectUrl;
        } else {
            targetUrl = request.getContextPath() + AppConstants.SERVLET_HOME;
        }

        response.sendRedirect(targetUrl);
    }

    /**
     * Validate redirect URL to prevent open redirect vulnerabilities
     */
    private boolean isValidRedirectUrl(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }

        // Only allow relative URLs starting with /
        if (!url.startsWith("/")) {
            return false;
        }

        // Prevent protocol-relative URLs
        if (url.startsWith("//")) {
            return false;
        }

        // Prevent double slashes and special characters
        if (url.contains("..") || url.contains("://")) {
            return false;
        }

        return true;
    }
}
