package org.example.mycute.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}") // 默认24小时
    private long jwtExpirationMs;

    private Key signingKey;

    @PostConstruct
    public void init() {
        // 确保密钥长度足够用于HS384
        byte[] keyBytes = jwtSecret.getBytes();
        if (keyBytes.length < 48) { // HS384需要至少48字节
            byte[] newKeyBytes = new byte[48];
            System.arraycopy(keyBytes, 0, newKeyBytes, 0, Math.min(keyBytes.length, 48));
            signingKey = Keys.hmacShaKeyFor(newKeyBytes);
        } else {
            signingKey = Keys.hmacShaKeyFor(keyBytes);
        }
        log.info("JWT signing key initialized for HS384");
    }

    private Key getSigningKey() {
        return signingKey;
    }

    public String generateToken(Integer userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
        
        String token = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS384)
                .compact();
        
        log.debug("Generated token with HS384: {}", token);
        return token;
    }

    public Integer getUserIdFromToken(String token) {
        log.debug("Parsing token: {}", token);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token.replace("\"", ""))
                .getBody();

        Integer userId = Integer.valueOf(claims.getSubject());
        log.debug("Extracted userId: {}", userId);
        return userId;
    }

    public boolean validateToken(String token) {
        try {
            log.debug("Validating token: {}", token);
            // 移除可能的引号
            token = token.replace("\"", "");
            
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            log.debug("Token validation successful");
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature for token: {}", token);
            log.error("Exception details:", ex);
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token format: {}", token);
            log.error("Exception details:", ex);
        } catch (ExpiredJwtException ex) {
            log.error("JWT token is expired: {}", token);
            log.error("Exception details:", ex);
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token: {}", token);
            log.error("Exception details:", ex);
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty: {}", token);
            log.error("Exception details:", ex);
        }
        return false;
    }
}
