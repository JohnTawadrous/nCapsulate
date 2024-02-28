package io.ncapsulate.letsbet.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.ncapsulate.letsbet.security.services.UserDetailsImpl;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${ncapsulate.app.jwtSecret}")
    private String jwtSecret;

    @Value("${ncapsulate.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${ncapsulate.app.jwtCookieName}")
    private String jwtCookie;


    /**
     * Retrieve JWT token from cookies in the HttpServletRequest.
     *
     * @param request The incoming HTTP request.
     * @return The JWT token or null if not found.
     */
    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }


    /**
     * Generate a JWT cookie for the given user details.
     *
     * @param userPrincipal User details for whom the token is generated.
     * @return A ResponseCookie containing the JWT.
     */
    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();
        return cookie;
    }


    /**
     * Generate a clean (expired) JWT cookie.
     *
     * @return A ResponseCookie with a null value.
     */
    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
        return cookie;
    }


    /**
     * Extract the username from a JWT token.
     *
     * @param token The JWT token.
     * @return The username extracted from the token.
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }


    /**
     * Generate the cryptographic key used to sign and verify JWT tokens.
     *
     * @return The cryptographic key.
     */
    private Key key() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);

        // Ensure that the key length is at least 256 bits
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT secret key must be at least 256 bits long");
        }
        // Use the first 256 bits of the keyBytes
        byte[] truncatedKey = Arrays.copyOf(keyBytes, 32);

        return Keys.hmacShaKeyFor(truncatedKey);
    }



    /**
     * Validate a JWT token.
     *
     * @param authToken The JWT token to validate.
     * @return True if the token is valid, false otherwise.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }


    /**
     * Generate a JWT token for a given username.
     *
     * @param username The username for whom the token is generated.
     * @return The generated JWT token.
     */
    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }
    /**
     * Generate a JWT token for a given UserDetailsImpl object.
     *
     * @param userDetails The UserDetailsImpl object for whom the token is generated.
     * @return The generated JWT token.
     */
    public String generateTokenFromUserDetails(UserDetailsImpl userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                // Add additional claims if needed
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }
}
