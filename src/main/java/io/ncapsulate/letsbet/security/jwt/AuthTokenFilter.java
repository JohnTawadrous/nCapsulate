package io.ncapsulate.letsbet.security.jwt;

import io.ncapsulate.letsbet.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * AuthTokenFilter is a custom filter responsible for intercepting incoming requests
 * to extract and validate JWT tokens from the request. If a valid token is found,
 * it sets the user's authentication details in the security context.
 */
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Extract JWT token from the request
            String jwt = parseJwt(request);

            // Validate the extracted JWT token
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // Extract username from the validated token
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // Load user details from the database based on the username
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Create an authentication token using the user details
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());

                // Set authentication details from the request
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }


    /**
     * Parses the JWT token from the request, typically retrieved from cookies.
     *
     * @param request The incoming HTTP request.
     * @return The extracted JWT token.
     */
    private String parseJwt(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromCookies(request);
        return jwt;
    }
}
