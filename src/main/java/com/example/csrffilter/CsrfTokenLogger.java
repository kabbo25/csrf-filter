package com.example.csrffilter;

import jakarta.servlet.*;
import org.springframework.security.web.csrf.CsrfToken;
import java.io.IOException;
import java.util.logging.Logger;

public class CsrfTokenLogger implements Filter {
    private final Logger logger = Logger.getLogger(CsrfTokenLogger.class.getName());





    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Object o = servletRequest.getAttribute("_csrf");
        if (o != null) {
            CsrfToken token = (CsrfToken) o;
            logger.info("CSRF token: " + token.getToken());
            System.out.println("CSRF token: " + token.getToken());
        } else {
            logger.info("No CSRF token found in request attributes");
            System.out.println("No CSRF token found in request attributes");
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
