package com.minishop.web.servlet;

import com.minishop.config.AppConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Home page servlet
 * Displays the main landing page of the application
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"/home", ""})
public class HomeServlet extends HttpServlet {

    /**
     * Handle GET requests for home page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set page title in request scope
        request.setAttribute("pageTitle", "Accueil - MiniShop");

        // Forward to home JSP
        request.getRequestDispatcher(AppConstants.JSP_HOME).forward(request, response);
    }

    /**
     * Handle POST requests - redirect to GET
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
