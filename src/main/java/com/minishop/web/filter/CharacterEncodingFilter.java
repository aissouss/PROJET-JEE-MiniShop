package com.minishop.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Character encoding filter
 * Ensures all requests and responses use UTF-8 encoding
 * This prevents character encoding issues with accented characters
 */
@WebFilter(filterName = "CharacterEncodingFilter", urlPatterns = {"/*"})
public class CharacterEncodingFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(CharacterEncodingFilter.class.getName());
    private static final String ENCODING = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("CharacterEncodingFilter initialized - encoding: " + ENCODING);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                        FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Set request encoding
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(ENCODING);
        }

        // Set response encoding
        response.setCharacterEncoding(ENCODING);

        // Continue the filter chain
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        LOGGER.info("CharacterEncodingFilter destroyed");
    }
}
