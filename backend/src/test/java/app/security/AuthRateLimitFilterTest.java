package app.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class AuthRateLimitFilterTest {
    @Test void registrationHasItsOwnLimit() throws Exception {
        AuthRateLimitFilter filter = new AuthRateLimitFilter(2, 60, 1, 60);
        MockHttpServletRequest first = new MockHttpServletRequest("POST", "/api/auth/register");
        first.setServletPath("/api/auth/register"); first.setRemoteAddr("192.0.2.20");
        filter.doFilter(first, new MockHttpServletResponse(), (request, response) -> { });
        MockHttpServletRequest second = new MockHttpServletRequest("POST", "/api/auth/register");
        second.setServletPath("/api/auth/register"); second.setRemoteAddr("192.0.2.20");
        MockHttpServletResponse limited = new MockHttpServletResponse();
        filter.doFilter(second, limited, (request, response) -> { });
        assertEquals(429, limited.getStatus());
    }

    @Test void returns429AtLimitAndIgnoresForwardedAddress() throws Exception {
        AuthRateLimitFilter filter = new AuthRateLimitFilter(2, 60, 2, 60);
        int[] passed = {0};
        FilterChain chain = (request, response) -> passed[0]++;
        for (int i = 0; i < 3; i++) {
            MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/auth/login");
            request.setServletPath("/api/auth/login");
            request.setRemoteAddr("192.0.2.10");
            request.addHeader("X-Forwarded-For", "198.51.100." + i);
            MockHttpServletResponse response = new MockHttpServletResponse();
            filter.doFilter(request, response, chain);
            if (i == 2) {
                assertEquals(429, response.getStatus());
                assertEquals("application/json", response.getContentType());
                assertEquals("Too many requests. Try again later.", response.getContentAsString().contains("Too many requests") ? "Too many requests. Try again later." : "");
            }
        }
        assertEquals(2, passed[0]);
    }
}

