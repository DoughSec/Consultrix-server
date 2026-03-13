package com.consultrix.consultrixserver.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/consultrix/auth");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        log.debug("JWT Filter – URI: {}, Authorization header present: {}", request.getRequestURI(), authHeader != null);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            boolean valid = jwtUtils.isTokenValid(token);
            log.debug("JWT Filter – Token valid: {}", valid);

            if (valid) {
                Claims claims = jwtUtils.decodeJWT(token);
                String userId = claims.getId();
                String role = claims.get("role", String.class);
                log.debug("JWT Filter – userId: {}, role from token: '{}'", userId, role);

                List<SimpleGrantedAuthority> authorities = (role != null)
                        ? List.of(new SimpleGrantedAuthority(role))
                        : List.of();

                log.debug("JWT Filter – Granted authorities: {}", authorities);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } else {
            log.debug("JWT Filter – No Bearer token found for URI: {}", request.getRequestURI());
        }

        filterChain.doFilter(request, response);
    }
}


