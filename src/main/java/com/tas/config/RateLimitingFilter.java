package com.tas.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final ConcurrentHashMap<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> requestTimes = new ConcurrentHashMap<>();
    private final int MAX_REQUESTS = 100; // per minute
    private final long TIME_WINDOW = 60000; // 1 minute

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String clientIp = getClientIp(request);
        long currentTime = System.currentTimeMillis();

        // Reset counter if time window has passed
        Long lastRequestTime = requestTimes.get(clientIp);
        if (lastRequestTime == null || (currentTime - lastRequestTime) > TIME_WINDOW) {
            requestCounts.put(clientIp, new AtomicInteger(0));
            requestTimes.put(clientIp, currentTime);
        }

        AtomicInteger count = requestCounts.get(clientIp);
        if (count.incrementAndGet() > MAX_REQUESTS) {
            response.setStatus(429); // Too Many Requests
            response.getWriter().write("Rate limit exceeded");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}