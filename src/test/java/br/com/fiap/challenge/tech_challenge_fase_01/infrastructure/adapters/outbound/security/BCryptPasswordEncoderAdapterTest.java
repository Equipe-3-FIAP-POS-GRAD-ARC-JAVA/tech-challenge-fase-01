package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@DisplayName("BCryptPasswordEncoderAdapter Tests")
class BCryptPasswordEncoderAdapterTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private BCryptPasswordEncoderAdapter passwordEncoderAdapter;

    @Nested
    @DisplayName("Password Encoding Tests")
    class PasswordEncodingTests {

        @Test
        @DisplayName("Should encode valid password")
        void shouldEncodeValidPassword() {
            // Given
            String rawPassword = "password123";
            String encodedPassword = "$2a$10$encoded.password.hash";
            when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

            // When
            String result = passwordEncoderAdapter.encode(rawPassword);

            // Then
            assertNotNull(result);
            assertTrue(result.equals(encodedPassword));
            verify(passwordEncoder).encode(rawPassword);
        }

        @Test
        @DisplayName("Should encode different passwords to different hashes")
        void shouldEncodeDifferentPasswordsToDifferentHashes() {
            // Given
            String password1 = "password123";
            String password2 = "password456";
            String encoded1 = "$2a$10$encoded.password1.hash";
            String encoded2 = "$2a$10$encoded.password2.hash";
            
            when(passwordEncoder.encode(password1)).thenReturn(encoded1);
            when(passwordEncoder.encode(password2)).thenReturn(encoded2);

            // When
            String result1 = passwordEncoderAdapter.encode(password1);
            String result2 = passwordEncoderAdapter.encode(password2);

            // Then
            assertNotNull(result1);
            assertNotNull(result2);
            assertNotEquals(result1, result2);
            verify(passwordEncoder).encode(password1);
            verify(passwordEncoder).encode(password2);
        }

        @Test
        @DisplayName("Should throw exception when password is null")
        void shouldThrowExceptionWhenPasswordIsNull() {
            // Given
            String nullPassword = null;

            // When & Then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> passwordEncoderAdapter.encode(nullPassword)
            );
            
            assertTrue(exception.getMessage().contains("Senha não pode ser nula ou vazia"));
        }

        @Test
        @DisplayName("Should throw exception when password is empty")
        void shouldThrowExceptionWhenPasswordIsEmpty() {
            // Given
            String emptyPassword = "";

            // When & Then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> passwordEncoderAdapter.encode(emptyPassword)
            );
            
            assertTrue(exception.getMessage().contains("Senha não pode ser nula ou vazia"));
        }

        @Test
        @DisplayName("Should throw exception when password is blank")
        void shouldThrowExceptionWhenPasswordIsBlank() {
            // Given
            String blankPassword = "   ";

            // When & Then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> passwordEncoderAdapter.encode(blankPassword)
            );
            
            assertTrue(exception.getMessage().contains("Senha não pode ser nula ou vazia"));
        }

        @Test
        @DisplayName("Should encode password with special characters")
        void shouldEncodePasswordWithSpecialCharacters() {
            // Given
            String complexPassword = "P@ssw0rd!@#$%";
            String encodedPassword = "$2a$10$encoded.complex.password.hash";
            when(passwordEncoder.encode(complexPassword)).thenReturn(encodedPassword);

            // When
            String result = passwordEncoderAdapter.encode(complexPassword);

            // Then
            assertNotNull(result);
            assertTrue(result.equals(encodedPassword));
            verify(passwordEncoder).encode(complexPassword);
        }
    }

    @Nested
    @DisplayName("Password Matching Tests")
    class PasswordMatchingTests {

        @Test
        @DisplayName("Should return true when passwords match")
        void shouldReturnTrueWhenPasswordsMatch() {
            // Given
            String rawPassword = "password123";
            String encodedPassword = "$2a$10$encoded.password.hash";
            when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

            // When
            boolean result = passwordEncoderAdapter.matches(rawPassword, encodedPassword);

            // Then
            assertTrue(result);
            verify(passwordEncoder).matches(rawPassword, encodedPassword);
        }

        @Test
        @DisplayName("Should return false when passwords don't match")
        void shouldReturnFalseWhenPasswordsDontMatch() {
            // Given
            String rawPassword = "password123";
            String encodedPassword = "$2a$10$encoded.different.password.hash";
            when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

            // When
            boolean result = passwordEncoderAdapter.matches(rawPassword, encodedPassword);

            // Then
            assertFalse(result);
            verify(passwordEncoder).matches(rawPassword, encodedPassword);
        }

        @Test
        @DisplayName("Should return false when raw password is null")
        void shouldReturnFalseWhenRawPasswordIsNull() {
            // Given
            String nullRawPassword = null;
            String encodedPassword = "$2a$10$encoded.password.hash";

            // When
            boolean result = passwordEncoderAdapter.matches(nullRawPassword, encodedPassword);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false when encoded password is null")
        void shouldReturnFalseWhenEncodedPasswordIsNull() {
            // Given
            String rawPassword = "password123";
            String nullEncodedPassword = null;

            // When
            boolean result = passwordEncoderAdapter.matches(rawPassword, nullEncodedPassword);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should return false when both passwords are null")
        void shouldReturnFalseWhenBothPasswordsAreNull() {
            // Given
            String nullRawPassword = null;
            String nullEncodedPassword = null;

            // When
            boolean result = passwordEncoderAdapter.matches(nullRawPassword, nullEncodedPassword);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should handle empty strings correctly")
        void shouldHandleEmptyStringsCorrectly() {
            // Given
            String emptyRawPassword = "";
            String emptyEncodedPassword = "";
            when(passwordEncoder.matches(emptyRawPassword, emptyEncodedPassword)).thenReturn(false);

            // When
            boolean result = passwordEncoderAdapter.matches(emptyRawPassword, emptyEncodedPassword);

            // Then
            assertFalse(result);
            verify(passwordEncoder).matches(emptyRawPassword, emptyEncodedPassword);
        }

        @Test
        @DisplayName("Should delegate complex matching to Spring Security encoder")
        void shouldDelegateComplexMatchingToSpringSecurityEncoder() {
            // Given
            String complexPassword = "MyV3ry$ecur3P@ssw0rd!123";
            String complexEncodedPassword = "$2a$12$very.complex.encoded.password.hash.with.salt";
            when(passwordEncoder.matches(complexPassword, complexEncodedPassword)).thenReturn(true);

            // When
            boolean result = passwordEncoderAdapter.matches(complexPassword, complexEncodedPassword);

            // Then
            assertTrue(result);
            verify(passwordEncoder).matches(complexPassword, complexEncodedPassword);
        }
    }
}