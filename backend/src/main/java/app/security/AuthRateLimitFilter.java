package app.security;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

/** Process-local fixed-window limiter for login and registration. */
public final class AuthRateLimitFilter extends OncePerRequestFilter {
    private final int loginMax;
    private final long loginWindowMs;
    private final int registerMax;
    private final long registerWindowMs;
    private static final int MAX_TRACKED_KEYS = 10000;
    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final AtomicLong requests = new AtomicLong();

    public AuthRateLimitFilter(int loginMax, long loginWindowSeconds, int registerMax, long registerWindowSeconds) {
        if (loginMax < 1 || registerMax < 1 || loginWindowSeconds < 1 || registerWindowSeconds < 1) throw new IllegalArgumentException("Rate limit settings must be positive.");
        this.loginMax = loginMax; this.loginWindowMs = loginWindowSeconds * 1000L;
        this.registerMax = registerMax; this.registerWindowMs = registerWindowSeconds * 1000L;
    }

    @Override protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return !"POST".equalsIgnoreCase(request.getMethod()) || !("/api/auth/login".equals(path) || "/api/auth/register".equals(path));
    }

    @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String path = request.getServletPath();
        int limit = "/api/auth/login".equals(path) ? loginMax : registerMax;
        long window = "/api/auth/login".equals(path) ? loginWindowMs : registerWindowMs;
        long now = System.currentTimeMillis();
        String key = path + "|" + request.getRemoteAddr(); // deliberately ignores forwarded-IP headers
        if (!buckets.containsKey(key) && buckets.size() >= MAX_TRACKED_KEYS) {
            buckets.entrySet().removeIf(entry -> now - entry.getValue().startedAt >= entry.getValue().windowMs);
            if (buckets.size() >= MAX_TRACKED_KEYS) {
                response.setStatus(429);
                response.setHeader("Retry-After", "60");
                response.setContentType("application/json");
                response.getWriter().write("{\"status\":429,\"message\":\"Too many requests. Try again later.\"}");
                return;
            }
        }
        AtomicBoolean allowed = new AtomicBoolean();
        AtomicLong retrySeconds = new AtomicLong(1);
        buckets.compute(key, (ignored, bucket) -> {
            if (bucket == null || now - bucket.startedAt >= window) {
                allowed.set(true);
                return new Bucket(now, 1, window);
            }
            if (bucket.count < limit) {
                bucket.count++;
                allowed.set(true);
                return bucket;
            }
            retrySeconds.set(Math.max(1, (window - (now - bucket.startedAt) + 999) / 1000));
            return bucket;
        });
        if (requests.incrementAndGet() % 256 == 0) {
            long timestamp = now;
            buckets.entrySet().removeIf(entry -> timestamp - entry.getValue().startedAt >= entry.getValue().windowMs);
        }
        if (!allowed.get()) {
            response.setStatus(429);
            response.setHeader("Retry-After", Long.toString(retrySeconds.get()));
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":429,\"message\":\"Too many requests. Try again later.\"}");
            return;
        }
        chain.doFilter(request, response);
    }

    private static final class Bucket {
        private final long startedAt;
        private int count;
        private final long windowMs;
        private Bucket(long startedAt, int count, long windowMs) { this.startedAt = startedAt; this.count = count; this.windowMs = windowMs; }
    }
}



