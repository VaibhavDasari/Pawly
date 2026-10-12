package app.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expirationTime;

    public JwtService(JwtSettings settings) {
        String secret = settings.secret();
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32
                || java.util.List.of("replace", "change", "example", "password", "pawly", "secret")
                    .stream().anyMatch(marker -> secret.toLowerCase(java.util.Locale.ROOT).contains(marker))) {
            throw new IllegalStateException("JWT_SECRET must contain at least 32 UTF-8 bytes; generate a random secret before starting Pawly.");
        }
        if (settings.expiration() < 60_000) {
            throw new IllegalStateException("JWT_EXPIRATION_MS must be at least 60000 milliseconds.");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = settings.expiration();
    }

    public String generateToken(String email) {
        long now = System.currentTimeMillis();
        return Jwts.builder().subject(email).issuedAt(new Date(now))
                .expiration(new Date(now + expirationTime)).signWith(key).compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException exception) {
            return false;
        }
    }
}


