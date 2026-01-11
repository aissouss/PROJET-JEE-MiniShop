package com.minishop.web.servlet.auth;

import com.minishop.config.AppConstants;
import com.minishop.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Logout servlet
 * Handles user logout by invalidating the session
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {AppConstants.SERVLET_LOGOUT})
public class LogoutServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LogoutServlet.class.getName());

    /**
     * Handle logout for both GET and POST requests
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        performLogout(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        performLogout(request, response);
    }

    /**
     * Perform the logout operation
     */
    private void performLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            // Get user for logging before invalidating session
            User user = (User) session.getAttribute(AppConstants.AUTH_USER);

            if (user != null) {
                LOGGER.info("User logging out: " + user.getEmail());
            }

            // Invalidate the session
            session.invalidate();
        }

        // Create new session for flash message
        HttpSession newSession = request.getSession(true);
        newSession.setAttribute(AppConstants.SESSION_INFO_MESSAGE,
                "Vous avez été déconnecté avec succès");

        // Redirect to home page
        response.sendRedirect(request.getContextPath() + AppConstants.SERVLET_HOME);
    }
}
