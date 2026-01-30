package com.projetoDac.feedback_alunos.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
public class JwtUtils {

    public static final String JWT_AUTHORIZATION = "Authorization";
    public static final String JWT_BEARER = "Bearer";
    public static final String SECRET_KEY = "0123456789-0123456789-0123456789";
    public static final Long EXPIRE_DAYS = 0L;
    public static final Long EXPIRE_HOURS = 0L;
    public static final Long EXPIRE_MINUTES = Duration.ofHours(1).toMillis();


    private JwtUtils(){

    }

    private static Key generatyKey(){

        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    private static Date toExpireDate(Date start){
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime and = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);
        return Date.from(and.atZone(ZoneId.systemDefault()).toInstant());
    }


    public static JwtToken createToken(String matricula) {
        Date issuedAt = new Date();
        Date limit = toExpireDate(issuedAt);

        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(matricula)
                .setIssuedAt(issuedAt)
                .setExpiration(limit)
                .signWith(generatyKey(), SignatureAlgorithm.HS256)
                .compact();

        return new JwtToken(token);
    }

    private static Claims getClaimsFromToken(String token){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(generatyKey()).build()
                    .parseClaimsJws(refactorToken(token)).getBody();

        }catch (JwtException e){
            log.error(String.format("ERRO: Invalid token %s", e.getMessage()));
        }
        return null;
    }

    public static String getUserNameFromTokem(String token){

        return getClaimsFromToken(token).getSubject();
    }

    public static boolean isTokenValid(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(generatyKey()).build()
                    .parseClaimsJws(refactorToken(token));
            return true;
        }catch (JwtException e){
            log.error(String.format("ERRO: Invalid token %s", e.getMessage()));
        }
        return false;
    }

    private static String refactorToken(String token){
        if(token.contains(JWT_BEARER)){
            return token.substring(JWT_BEARER.length());
        }
        return token;
    }
}
