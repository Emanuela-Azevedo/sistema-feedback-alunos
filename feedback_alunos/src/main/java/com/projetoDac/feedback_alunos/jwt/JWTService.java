package com.projetoDac.feedback_alunos.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${jwtSecret}")
    private String jwtSecret;
    @Value("${jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateToken(Authentication authentication){
        return Jwts.builder()
                .subject(authentication.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders
                .BASE64
                .decode(
                        new String(jwtSecret.getBytes(StandardCharsets.UTF_8)));
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }
    public boolean validateJwtToken(String token, String username) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parse(token);
            return extractUsername(token).equals(username);
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: {}" + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: {}" + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: {}" + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: {}" + e.getMessage());
        }
        return false;
    }
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
        
    }

    private <T> T extractClaim(String token, Function <Claims , T>  claimsResolver) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }
}
