package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("JwtUtil Tests")
class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Nested
    @DisplayName("Token Generation Tests")
    class TokenGenerationTests {

        @Test
        @DisplayName("Should generate valid token with username and roles")
        void shouldGenerateValidTokenWithUsernameAndRoles() {
            // Given
            String username = "testuser";
            List<String> roles = List.of("USER", "ADMIN");

            // When
            String token = jwtUtil.generateToken(username, roles);

            // Then
            assertNotNull(token);
            assertTrue(token.length() > 0);
            assertTrue(token.contains(".")); // JWT format has dots
        }

        @Test
        @DisplayName("Should generate different tokens for different users")
        void shouldGenerateDifferentTokensForDifferentUsers() {
            // Given
            String username1 = "user1";
            String username2 = "user2";
            List<String> roles = List.of("USER");

            // When
            String token1 = jwtUtil.generateToken(username1, roles);
            String token2 = jwtUtil.generateToken(username2, roles);

            // Then
            assertNotNull(token1);
            assertNotNull(token2);
            assertTrue(!token1.equals(token2));
        }

        @Test
        @DisplayName("Should generate token with empty roles")
        void shouldGenerateTokenWithEmptyRoles() {
            // Given
            String username = "testuser";
            List<String> roles = List.of();

            // When
            String token = jwtUtil.generateToken(username, roles);

            // Then
            assertNotNull(token);
            assertTrue(token.length() > 0);
        }

        @Test
        @DisplayName("Should generate token with multiple roles")
        void shouldGenerateTokenWithMultipleRoles() {
            // Given
            String username = "admin";
            List<String> roles = List.of("USER", "ADMIN", "OWNER");

            // When
            String token = jwtUtil.generateToken(username, roles);

            // Then
            assertNotNull(token);
            assertTrue(token.length() > 0);
        }
    }

    @Nested
    @DisplayName("Token Validation Tests")
    class TokenValidationTests {

        @Test
        @DisplayName("Should validate correct token as true")
        void shouldValidateCorrectTokenAsTrue() {
            // Given
            String username = "testuser";
            List<String> roles = List.of("USER");
            String token = jwtUtil.generateToken(username, roles);

            // When
            boolean isValid = jwtUtil.validateToken(token);

            // Then
            assertTrue(isValid);
        }

        @Test
        @DisplayName("Should validate invalid token as false")
        void shouldValidateInvalidTokenAsFalse() {
            // Given
            String invalidToken = "invalid.token.here";

            // When
            boolean isValid = jwtUtil.validateToken(invalidToken);

            // Then
            assertFalse(isValid);
        }

        @Test
        @DisplayName("Should validate empty token as false")
        void shouldValidateEmptyTokenAsFalse() {
            // Given
            String emptyToken = "";

            // When
            boolean isValid = jwtUtil.validateToken(emptyToken);

            // Then
            assertFalse(isValid);
        }

        @Test
        @DisplayName("Should validate null token as false")
        void shouldValidateNullTokenAsFalse() {
            // Given
            String nullToken = null;

            // When
            boolean isValid = jwtUtil.validateToken(nullToken);

            // Then
            assertFalse(isValid);
        }

        @Test
        @DisplayName("Should validate malformed token as false")
        void shouldValidateMalformedTokenAsFalse() {
            // Given
            String malformedToken = "eyJhbGciOiJIUzI1NiJ9.malformed";

            // When
            boolean isValid = jwtUtil.validateToken(malformedToken);

            // Then
            assertFalse(isValid);
        }
    }

    @Nested
    @DisplayName("Username Extraction Tests")
    class UsernameExtractionTests {

        @Test
        @DisplayName("Should extract username from valid token")
        void shouldExtractUsernameFromValidToken() {
            // Given
            String username = "testuser";
            List<String> roles = List.of("USER");
            String token = jwtUtil.generateToken(username, roles);

            // When
            String extractedUsername = jwtUtil.getUsernameFromToken(token);

            // Then
            assertEquals(username, extractedUsername);
        }

        @Test
        @DisplayName("Should extract different usernames from different tokens")
        void shouldExtractDifferentUsernamesFromDifferentTokens() {
            // Given
            String username1 = "user1";
            String username2 = "user2";
            List<String> roles = List.of("USER");
            String token1 = jwtUtil.generateToken(username1, roles);
            String token2 = jwtUtil.generateToken(username2, roles);

            // When
            String extractedUsername1 = jwtUtil.getUsernameFromToken(token1);
            String extractedUsername2 = jwtUtil.getUsernameFromToken(token2);

            // Then
            assertEquals(username1, extractedUsername1);
            assertEquals(username2, extractedUsername2);
        }

        @Test
        @DisplayName("Should extract username with special characters")
        void shouldExtractUsernameWithSpecialCharacters() {
            // Given
            String username = "user@example.com";
            List<String> roles = List.of("USER");
            String token = jwtUtil.generateToken(username, roles);

            // When
            String extractedUsername = jwtUtil.getUsernameFromToken(token);

            // Then
            assertEquals(username, extractedUsername);
        }
    }

    @Nested
    @DisplayName("Configuration Tests")
    class ConfigurationTests {

        @Test
        @DisplayName("Should return correct expiration in seconds")
        void shouldReturnCorrectExpirationInSeconds() {
            // Given & When
            long expirationInSeconds = jwtUtil.getExpirationInSeconds();

            // Then
            assertEquals(86400, expirationInSeconds); // 24 hours in seconds
        }

        @Test
        @DisplayName("Should use consistent expiration across multiple calls")
        void shouldUseConsistentExpirationAcrossMultipleCalls() {
            // Given & When
            long expiration1 = jwtUtil.getExpirationInSeconds();
            long expiration2 = jwtUtil.getExpirationInSeconds();

            // Then
            assertEquals(expiration1, expiration2);
        }
    }
}