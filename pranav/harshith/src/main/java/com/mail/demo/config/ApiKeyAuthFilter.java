package com.mail.demo.config;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Simple API  key filter that protects every endpoint except the public ones listed in {@link SecurityConfig}.
 */
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final String apiKey;
    private final List<AntPathRequestMatcher> excludedMatchers;

    public ApiKeyAuthFilter(String apiKey, List<String> excludedPaths) {
        this.apiKey = apiKey;
        this.excludedMatchers = excludedPaths.stream()
                .map(AntPathRequestMatcher::new)
                .toList();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return excludedMatchers.stream().anyMatch(matcher -> matcher.matches(request));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String providedKey = request.getHeader("X-API-KEY");

        if (providedKey != null && providedKey.equals(apiKey)) {
            var authentication = new UsernamePasswordAuthenticationToken(
                    "api-key-client",
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_API"))
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Missing or invalid API key\"}");
    }
}

