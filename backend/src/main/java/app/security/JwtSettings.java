package app.security;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public record JwtSettings(@Value("${jwt.secret:}") String secret, @Value("${jwt.expiration:86400000}") long expiration) { }
