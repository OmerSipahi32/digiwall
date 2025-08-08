package com.inghubs.digiwall.config;

import com.inghubs.digiwall.model.entity.Customer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final String DEFAULT_SECRET = "u7Q!z9@w4#e8^r2$k6&b1*V0p3L5m7N9s2T4x6A8d0F2g4H";
    private static final String ENV_SECRET = System.getenv("JWT_SECRET_KEY");
    private final Key key = Keys.hmacShaKeyFor(
            (ENV_SECRET != null && ENV_SECRET.length() >= 32 ? ENV_SECRET : DEFAULT_SECRET)
                    .getBytes(StandardCharsets.UTF_8));

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Long getCustomerId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("customerId", Long.class);
    }

    public String getRole(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
    }

    public String createToken(Customer customer) {
        return Jwts.builder()
                .claim("customerId", customer.getId())
                .claim("role", customer.getRole())
                .setSubject(customer.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 gün
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}