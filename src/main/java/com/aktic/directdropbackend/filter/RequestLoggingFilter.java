package com.aktic.directdropbackend.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // Ensure this filter runs before Spring Security
public class RequestLoggingFilter extends GenericFilterBean {

    private static final Logger requestLogger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        long startTime = System.currentTimeMillis();

        chain.doFilter(request, response); // Continue the filter chain

        // Log after request processing
        long duration = System.currentTimeMillis() - startTime;
        String method = httpRequest.getMethod();
        String uri = httpRequest.getRequestURI();
        int status = httpResponse.getStatus(); // Capture final response status

        requestLogger.info("{} {} {} {}ms", method, uri, status, duration);
    }
}
