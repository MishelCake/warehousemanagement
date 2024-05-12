package com.example.warehousemanagement.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration.seconds}")
    private Long jwtTokenValidity;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String generateToken(UserDetailsImpl userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name",userDetails.getName());
        claims.put("surname",userDetails.getSurname());
        claims.put("userId", userDetails.getUserId());

        Optional<String> authority = userDetails.getAuthorities().stream().findFirst().map(Object::toString);
        String result = authority.map(s -> s.replaceFirst("^ROLE_", "")).orElse("");
        claims.put("role", result);

        return buildToken(claims, userDetails.getUsername());
    }

    private String buildToken(Map<String, Object> claims, String subject) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        Calendar issuedAt = Calendar.getInstance();
        Calendar exp = Calendar.getInstance();
        exp.setTimeInMillis(issuedAt.getTimeInMillis() + (jwtTokenValidity*1000));
        return Jwts
                .builder()
                .signWith(key)
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(issuedAt.getTime())
                .setExpiration(exp.getTime())
                .signWith(key, SignatureAlgorithm.HS512).compact();
    }

    public Boolean validateToken(String token) {
        try {
            getAllClaimsFromToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
