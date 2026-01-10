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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Registration servlet
 * Handles new user registration
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {AppConstants.SERVLET_REGISTER})
public class RegisterServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(RegisterServlet.class.getName());
    private AuthService authService;

    @Override
    public void init() throws ServletException {
        super.init();
        authService = AuthService.getInstance();
    }

    /**
     * Display registration form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // If user is already logged in, redirect to home
        if (session != null && session.getAttribute(AppConstants.SESSION_USER) != null) {
            response.sendRedirect(request.getContextPath() + AppConstants.SERVLET_HOME);
            return;
        }

        // Forward to registration page
        request.getRequestDispatcher(AppConstants.JSP_REGISTER).forward(request, response);
    }

    /**
     * Process registration form submission
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get form parameters
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validate input
        List<String> errors = validateRegistrationInput(fullName, email, password, confirmPassword);

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("fullName", fullName);
            request.setAttribute("email", email);
            request.getRequestDispatcher(AppConstants.JSP_REGISTER).forward(request, response);
            return;
        }

        // Attempt to register user
        Optional<User> userOptional = authService.register(email, password, fullName);

        if (userOptional.isEmpty()) {
            errors.add("L'adresse email est déjà utilisée");
            request.setAttribute("errors", errors);
            request.setAttribute("fullName", fullName);
            request.setAttribute("email", email);
            request.getRequestDispatcher(AppConstants.JSP_REGISTER).forward(request, response);
            return;
        }

        User user = userOptional.get();

        LOGGER.info("New user registered: " + user.getEmail());

        // Auto-login the user after successful registration
        HttpSession session = request.getSession(true);
        session.setAttribute(AppConstants.SESSION_USER, user);
        session.setAttribute(AppConstants.SESSION_SUCCESS_MESSAGE,
                "Bienvenue sur MiniShop, " + user.getFullName() + " ! Votre compte a été créé avec succès.");

        // Redirect to home page
        response.sendRedirect(request.getContextPath() + AppConstants.SERVLET_HOME);
    }

    /**
     * Validate registration input
     */
    private List<String> validateRegistrationInput(String fullName, String email,
                                                   String password, String confirmPassword) {
        List<String> errors = new ArrayList<>();

        // Validate full name
        if (fullName == null || fullName.trim().isEmpty()) {
            errors.add("Le nom complet est requis");
        } else if (fullName.trim().length() < AppConstants.MIN_NAME_LENGTH) {
            errors.add("Le nom doit contenir au moins " + AppConstants.MIN_NAME_LENGTH + " caractères");
        } else if (fullName.trim().length() > AppConstants.MAX_NAME_LENGTH) {
            errors.add("Le nom ne peut pas dépasser " + AppConstants.MAX_NAME_LENGTH + " caractères");
        }

        // Validate email
        if (email == null || email.trim().isEmpty()) {
            errors.add("L'adresse email est requise");
        } else if (!isValidEmail(email)) {
            errors.add("L'adresse email n'est pas valide");
        }

        // Validate password
        if (password == null || password.isEmpty()) {
            errors.add("Le mot de passe est requis");
        } else if (password.length() < AppConstants.MIN_PASSWORD_LENGTH) {
            errors.add("Le mot de passe doit contenir au moins " + AppConstants.MIN_PASSWORD_LENGTH + " caractères");
        } else if (password.length() > AppConstants.MAX_PASSWORD_LENGTH) {
            errors.add("Le mot de passe ne peut pas dépasser " + AppConstants.MAX_PASSWORD_LENGTH + " caractères");
        }

        // Validate password confirmation
        if (confirmPassword == null || confirmPassword.isEmpty()) {
            errors.add("La confirmation du mot de passe est requise");
        } else if (!password.equals(confirmPassword)) {
            errors.add("Les mots de passe ne correspondent pas");
        }

        return errors;
    }

    /**
     * Validate email format
     */
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}
