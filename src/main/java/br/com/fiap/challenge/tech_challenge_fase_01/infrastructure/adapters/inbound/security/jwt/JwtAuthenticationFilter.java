package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.security.jwt;

import java.io.IOException;
import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.configs.constants.SecurityConstants;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenManager jwtTokenManager;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws IOException, ServletException {

        try {
            Optional<String> tokenOptional = extractTokenFromRequest(request);

            if (tokenOptional.isEmpty()) {
                continueFilterChain(request, response, chain);
                return;
            }

            String token = tokenOptional.get();
            Optional<String> usernameOptional = extractUsernameFromToken(token);

            if (usernameOptional.isEmpty()) {
                continueFilterChain(request, response, chain);
                return;
            }

            String username = usernameOptional.get();

            if (shouldSkipAuthentication()) {
                continueFilterChain(request, response, chain);
                return;
            }

            Optional<UserDetails> userOptional = loadAndValidateUser(username, token);

            if (userOptional.isPresent()) {
                authenticateUser(request, userOptional.get());
                log.info("Authentication successful for user: {}", username);
            }

        } catch (ExpiredJwtException e) {
            log.warn("JWT token expired: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT token format: {}", e.getMessage());
        } catch (SecurityException e) {
            log.warn("JWT signature validation failed: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected authentication error: {}", e.getMessage());
        }

        continueFilterChain(request, response, chain);
    }

    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader(SecurityConstants.HEADER_STRING);
        if (header != null && header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return Optional.of(header.replace(SecurityConstants.TOKEN_PREFIX, Strings.EMPTY));
        }
        return Optional.empty();
    }

    private Optional<String> extractUsernameFromToken(String token) {
        try {
            return Optional.ofNullable(jwtTokenManager.getUsernameFromToken(token));
        } catch (Exception e) {
            log.debug("Failed to extract username from token: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private boolean shouldSkipAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }

    private Optional<UserDetails> loadAndValidateUser(String username, String token) {
        try {
            UserDetails user = userDetailsService.loadUserByUsername(username);

            if (!user.isEnabled() || !user.isAccountNonExpired() ||
                !user.isAccountNonLocked() || !user.isCredentialsNonExpired()) {
                log.warn("User {} is not active or valid", username);
                return Optional.empty();
            }

            if (!jwtTokenManager.validateToken(token, user.getUsername())) {
                log.warn("Invalid token for user: {}", username);
                return Optional.empty();
            }

            return Optional.of(user);
        } catch (Exception e) {
            log.error("Failed to load or validate user {}: {}", username, e.getMessage());
            return Optional.empty();
        }
    }

    private void authenticateUser(HttpServletRequest request, UserDetails user) {
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void continueFilterChain(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);
    }
}
