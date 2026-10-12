package app.security;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;

class JwtServiceTest {
    private static final String TEST_SECRET = "d7be15f7a8c64a279b71c9d2a194f35c49c0f1299e703a15";

    @Test void acceptsValidSignatureAndRejectsTampering() {
        JwtService service = new JwtService(new JwtSettings(TEST_SECRET, 60_000));
        String token = service.generateToken("user@example.test");
        assertTrue(service.isTokenValid(token));
        assertEquals("user@example.test", service.extractEmail(token));
        assertFalse(service.isTokenValid(token + "tampered"));
    }

    @Test void rejectsExpiredSignedToken() {
        var key = Keys.hmacShaKeyFor(TEST_SECRET.getBytes(StandardCharsets.UTF_8));
        String expired = Jwts.builder().subject("user@example.test").expiration(new Date(0)).signWith(key).compact();
        assertFalse(new JwtService(new JwtSettings(TEST_SECRET, 60_000)).isTokenValid(expired));
    }

    @Test void failsFastForMissingOrWeakSecret() {
        assertThrows(IllegalStateException.class, () -> new JwtService(new JwtSettings("", 60_000)));
        assertThrows(IllegalStateException.class, () -> new JwtService(new JwtSettings("too-short", 60_000)));
    }
}

