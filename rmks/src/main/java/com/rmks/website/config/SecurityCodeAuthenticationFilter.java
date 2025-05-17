package com.rmks.website.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class SecurityCodeAuthenticationFilter extends OncePerRequestFilter {

    private final String securityCode;

    public SecurityCodeAuthenticationFilter(String securityCode) {
        this.securityCode = securityCode;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        if (request.getRequestURI().equals("/login") && request.getMethod().equals("POST")) {
            String submittedCode = request.getParameter("securityCode");
            
            if (!securityCode.equals(submittedCode)) {
                response.sendRedirect("/login?error=invalidCode");
                return;
            }
        }
        
        filterChain.doFilter(request, response);
    }
} 