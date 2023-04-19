package com.epam.restaurant.controller.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * The ${@code CharsetFilter} class is used to set the encoding value for requests and responses
 */
@WebFilter("/*")
public class CharsetFilter implements Filter {
    private static final Logger LOGGER = LogManager.getLogger(CharsetFilter.class);
    private static final String ENCODING = "utf-8";

    @Override
    public void init(FilterConfig filterConfig) {
        LOGGER.info("CharsetFilter is initialized");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding(ENCODING);
        servletResponse.setCharacterEncoding(ENCODING);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        LOGGER.info("CharsetFilter is destroyed");
    }
}
