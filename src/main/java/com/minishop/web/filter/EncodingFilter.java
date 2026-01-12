package com.minishop.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Encoding filter
 * Forces UTF-8 encoding for all requests and responses to handle accented characters properly
 */
@WebFilter(filterName = "EncodingFilter", urlPatterns = {"/*"})
public class EncodingFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(EncodingFilter.class.getName());
    private static final String ENCODING = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("EncodingFilter initialized - forcing UTF-8 encoding for all requests/responses");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                        FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Force UTF-8 encoding for request (form data, parameters)
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(ENCODING);
        }

        // Force UTF-8 encoding for response (HTML output, JSON)
        response.setCharacterEncoding(ENCODING);

        // Continue with the filter chain
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        LOGGER.info("EncodingFilter destroyed");
    }
}
