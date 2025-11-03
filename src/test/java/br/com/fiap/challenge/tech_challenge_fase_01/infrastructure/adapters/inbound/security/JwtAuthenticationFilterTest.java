package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.RolesEnum;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.entities.JpaUserEntity;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.repositories.user.JpaUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtAuthenticationFilter Tests")
class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private JpaUserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private SecurityContext securityContext;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil, userRepository);
        SecurityContextHolder.setContext(securityContext);
    }

    @Nested
    @DisplayName("Valid JWT Authentication Tests")
    class ValidJwtAuthenticationTests {

        @Test
        @DisplayName("Should authenticate user with valid JWT token")
        void shouldAuthenticateUserWithValidJwtToken() throws ServletException, IOException {
            // Given
            String token = "valid.jwt.token";
            String username = "testuser";
            String authHeader = "Bearer " + token;

            UUID userId = UUID.randomUUID();
            JpaUserEntity userEntity = new JpaUserEntity(
                    userId, "Test User", "test@example.com",
                    username, "password", null, null,
                    List.of(RolesEnum.CLIENT), true
            );

            when(request.getHeader("Authorization")).thenReturn(authHeader);
            when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);
            when(securityContext.getAuthentication()).thenReturn(null);
            when(userRepository.findByLogin(username)).thenReturn(Optional.of(userEntity));
            when(jwtUtil.validateToken(token)).thenReturn(true);

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(securityContext).setAuthentication(any(Authentication.class));
            verify(filterChain).doFilter(request, response);
        }

        @Test
        @DisplayName("Should authenticate user with ADMIN role")
        void shouldAuthenticateUserWithAdminRole() throws ServletException, IOException {
            // Given
            String token = "admin.jwt.token";
            String username = "adminuser";
            String authHeader = "Bearer " + token;

            UUID userId = UUID.randomUUID();
            JpaUserEntity adminEntity = new JpaUserEntity(
                    userId, "Admin User", "admin@example.com",
                    username, "password", null, null,
                    List.of(RolesEnum.ADMIN), true
            );

            when(request.getHeader("Authorization")).thenReturn(authHeader);
            when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);
            when(securityContext.getAuthentication()).thenReturn(null);
            when(userRepository.findByLogin(username)).thenReturn(Optional.of(adminEntity));
            when(jwtUtil.validateToken(token)).thenReturn(true);

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(securityContext).setAuthentication(any(Authentication.class));
            verify(filterChain).doFilter(request, response);
        }

        @Test
        @DisplayName("Should authenticate user with multiple roles")
        void shouldAuthenticateUserWithMultipleRoles() throws ServletException, IOException {
            // Given
            String token = "multi.role.token";
            String username = "multiuser";
            String authHeader = "Bearer " + token;

            UUID userId = UUID.randomUUID();
            JpaUserEntity multiRoleEntity = new JpaUserEntity(
                    userId, "Multi Role User", "multi@example.com",
                    username, "password", null, null,
                    List.of(RolesEnum.CLIENT, RolesEnum.ADMIN, RolesEnum.OWNER), true
            );

            when(request.getHeader("Authorization")).thenReturn(authHeader);
            when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);
            when(securityContext.getAuthentication()).thenReturn(null);
            when(userRepository.findByLogin(username)).thenReturn(Optional.of(multiRoleEntity));
            when(jwtUtil.validateToken(token)).thenReturn(true);

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(securityContext).setAuthentication(any(Authentication.class));
            verify(filterChain).doFilter(request, response);
        }
    }

    @Nested
    @DisplayName("Invalid Authentication Tests")
    class InvalidAuthenticationTests {

        @Test
        @DisplayName("Should not authenticate when no Authorization header")
        void shouldNotAuthenticateWhenNoAuthorizationHeader() throws ServletException, IOException {
            // Given
            when(request.getHeader("Authorization")).thenReturn(null);

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(securityContext, never()).setAuthentication(any(Authentication.class));
            verify(filterChain).doFilter(request, response);
            verify(jwtUtil, never()).getUsernameFromToken(anyString());
        }

        @Test
        @DisplayName("Should not authenticate when Authorization header doesn't start with Bearer")
        void shouldNotAuthenticateWhenAuthorizationHeaderDoesntStartWithBearer() throws ServletException, IOException {
            // Given
            when(request.getHeader("Authorization")).thenReturn("Basic invalid.credentials");

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(securityContext, never()).setAuthentication(any(Authentication.class));
            verify(filterChain).doFilter(request, response);
            verify(jwtUtil, never()).getUsernameFromToken(anyString());
        }

        @Test
        @DisplayName("Should not authenticate when token is invalid")
        void shouldNotAuthenticateWhenTokenIsInvalid() throws ServletException, IOException {
            // Given
            String token = "invalid.jwt.token";
            String username = "testuser";
            String authHeader = "Bearer " + token;

            UUID userId = UUID.randomUUID();
            JpaUserEntity userEntity = new JpaUserEntity(
                    userId, "Test User", "test@example.com",
                    username, "password", null, null,
                    List.of(RolesEnum.CLIENT), true
            );

            when(request.getHeader("Authorization")).thenReturn(authHeader);
            when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);
            when(securityContext.getAuthentication()).thenReturn(null);
            when(userRepository.findByLogin(username)).thenReturn(Optional.of(userEntity));
            when(jwtUtil.validateToken(token)).thenReturn(false);

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(securityContext, never()).setAuthentication(any(Authentication.class));
            verify(filterChain).doFilter(request, response);
        }

        @Test
        @DisplayName("Should not authenticate when user not found")
        void shouldNotAuthenticateWhenUserNotFound() throws ServletException, IOException {
            // Given
            String token = "valid.jwt.token";
            String username = "nonexistentuser";
            String authHeader = "Bearer " + token;

            when(request.getHeader("Authorization")).thenReturn(authHeader);
            when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);
            when(securityContext.getAuthentication()).thenReturn(null);
            when(userRepository.findByLogin(username)).thenReturn(Optional.empty());

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(securityContext, never()).setAuthentication(any(Authentication.class));
            verify(filterChain).doFilter(request, response);
            verify(jwtUtil, never()).validateToken(anyString());
        }

        @Test
        @DisplayName("Should not authenticate when username is null")
        void shouldNotAuthenticateWhenUsernameIsNull() throws ServletException, IOException {
            // Given
            String token = "token.without.username";
            String authHeader = "Bearer " + token;

            when(request.getHeader("Authorization")).thenReturn(authHeader);
            when(jwtUtil.getUsernameFromToken(token)).thenReturn(null);

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(securityContext, never()).setAuthentication(any(Authentication.class));
            verify(filterChain).doFilter(request, response);
            verify(userRepository, never()).findByLogin(anyString());
        }

        @Test
        @DisplayName("Should not authenticate when user already authenticated")
        void shouldNotAuthenticateWhenUserAlreadyAuthenticated() throws ServletException, IOException {
            // Given
            String token = "valid.jwt.token";
            String username = "testuser";
            String authHeader = "Bearer " + token;

            Authentication existingAuth = org.mockito.Mockito.mock(Authentication.class);

            when(request.getHeader("Authorization")).thenReturn(authHeader);
            when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);
            when(securityContext.getAuthentication()).thenReturn(existingAuth);

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(securityContext, never()).setAuthentication(any(Authentication.class));
            verify(filterChain).doFilter(request, response);
            verify(userRepository, never()).findByLogin(anyString());
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle empty Bearer token")
        void shouldHandleEmptyBearerToken() throws ServletException, IOException {
            // Given
            when(request.getHeader("Authorization")).thenReturn("Bearer ");

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(filterChain).doFilter(request, response);
            verify(jwtUtil).getUsernameFromToken("");
        }

        @Test
        @DisplayName("Should handle Bearer token with only spaces")
        void shouldHandleBearerTokenWithOnlySpaces() throws ServletException, IOException {
            // Given
            when(request.getHeader("Authorization")).thenReturn("Bearer    ");

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(filterChain).doFilter(request, response);
            verify(jwtUtil).getUsernameFromToken("   ");
        }

        @Test
        @DisplayName("Should handle case-sensitive Bearer prefix")
        void shouldHandleCaseSensitiveBearerPrefix() throws ServletException, IOException {
            // Given
            when(request.getHeader("Authorization")).thenReturn("bearer valid.jwt.token");

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(filterChain).doFilter(request, response);
            verify(jwtUtil, never()).getUsernameFromToken(anyString());
        }

        @Test
        @DisplayName("Should handle Authorization header with extra spaces")
        void shouldHandleAuthorizationHeaderWithExtraSpaces() throws ServletException, IOException {
            // Given
            when(request.getHeader("Authorization")).thenReturn("  Bearer valid.jwt.token  ");

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(filterChain).doFilter(request, response);
            verify(jwtUtil, never()).getUsernameFromToken(anyString());
        }
    }
}