package com.eduardo.tribunalhub.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        final String authorizationHeader = request.getHeader("Authorization");
        logger.info("Request URI: " + request.getRequestURI()); // Add request path logging
        logger.info("Auth header: " + authorizationHeader);

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
                logger.info("Extracted username from token: " + username); // Add username logging
            } catch (Exception e) {
                logger.error("Erro ao extrair username do token JWT: " + e.getMessage());
                e.printStackTrace(); // Add stack trace for token extraction errors
            }
        } else {
            logger.warn("No Bearer token found in request"); // Add warning for missing token
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                logger.info("Loaded user details for: " + username);
                logger.info("User authorities: " + userDetails.getAuthorities());

                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.info("Token validation successful - Authentication set in SecurityContext");
                } else {
                    logger.error("Token validation failed"); // Add token validation failure logging
                }
            } catch (Exception e) {
                logger.error("Authentication error: " + e.getMessage());
                e.printStackTrace(); // Add stack trace for authentication errors
            }
        } else {
            logger.warn("SecurityContext already contains authentication: " + 
                (SecurityContextHolder.getContext().getAuthentication() != null)); // Add context check
        }

        filterChain.doFilter(request, response);
    }
}
