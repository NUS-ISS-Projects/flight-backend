package com.webapp.flightsearch.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public class JwtUtil {
    private static String jwtSecret = "FlightSecretKey"; // Replace with your secret key
    private static int jwtExpirationMs = 86400000; // Token validity in milliseconds

    public static String generateJwtToken(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        } else {
            throw new IllegalArgumentException("Authentication principal is not of expected type");
        }

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                // .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}
