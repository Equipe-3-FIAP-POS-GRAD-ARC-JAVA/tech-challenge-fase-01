package br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.InvalidFieldException;

@DisplayName("Username Value Object Tests")
class UsernameTest {

    @Test
    @DisplayName("Should create valid username")
    void shouldCreateValidUsername() {
        // given
        String usernameValue = "user123";

        // when
        Username username = Username.of(usernameValue);

        // then
        assertThat(username.getValue()).isEqualTo("user123");
        assertThat(username.toString()).isEqualTo("user123");
    }

    @Test
    @DisplayName("Should normalize username by trimming spaces and converting to lowercase")
    void shouldNormalizeUsername() {
        // given
        String usernameWithSpaces = "  USER123  ";
        String upperCaseUsername = "USER123";
        String mixedCaseUsername = "UsEr123";

        // when
        Username username1 = Username.of(usernameWithSpaces);
        Username username2 = Username.of(upperCaseUsername);
        Username username3 = Username.of(mixedCaseUsername);

        // then
        assertThat(username1.getValue()).isEqualTo("user123");
        assertThat(username2.getValue()).isEqualTo("user123");
        assertThat(username3.getValue()).isEqualTo("user123");
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {" ", "\t", "\n", "   "})
    @DisplayName("Should throw exception for null, empty or blank username")
    void shouldThrowExceptionForInvalidUsername(String invalidUsername) {
        // when & then
        assertThatThrownBy(() -> Username.of(invalidUsername))
                .isInstanceOf(InvalidFieldException.class)
                .hasFieldOrPropertyWithValue("fieldName", "login")
                .hasMessageContaining("Login é obrigatório");
    }

    @Test
    @DisplayName("Should throw exception for username too short")
    void shouldThrowExceptionForUsernameTooShort() {
        // given
        String shortUsername = "ab"; // 2 characters

        // when & then
        assertThatThrownBy(() -> Username.of(shortUsername))
                .isInstanceOf(InvalidFieldException.class)
                .hasFieldOrPropertyWithValue("fieldName", "login")
                .hasMessageContaining("Login deve ter no mínimo 3 caracteres");
    }

    @Test
    @DisplayName("Should throw exception for username too long")
    void shouldThrowExceptionForUsernameTooLong() {
        // given
        String longUsername = "a".repeat(51); // 51 characters

        // when & then
        assertThatThrownBy(() -> Username.of(longUsername))
                .isInstanceOf(InvalidFieldException.class)
                .hasFieldOrPropertyWithValue("fieldName", "login")
                .hasMessageContaining("Login deve ter no máximo 50 caracteres");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "-username", "_username", ".username", "+username",
            "username-", "username_", "username.", "username+",
            "user@name", "user#name", "user$name", "user%name",
            "user&name", "user*name", "user name", "user!name"
    })
    @DisplayName("Should throw exception for username with invalid format")
    void shouldThrowExceptionForInvalidUsernameFormat(String invalidUsername) {
        // when & then
        assertThatThrownBy(() -> Username.of(invalidUsername))
                .isInstanceOf(InvalidFieldException.class)
                .hasFieldOrPropertyWithValue("fieldName", "login")
                .hasMessageContaining("Login deve");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "user123",
            "test_user",
            "user-name",
            "user.name",
            "username1",
            "123user",
            "user123name",
            "a1b",
            "test.user-name_123",
            "validuser",
            "user_123-test.name"
    })
    @DisplayName("Should accept valid username formats")
    void shouldAcceptValidUsernameFormats(String validUsername) {
        // when
        Username username = Username.of(validUsername);

        // then
        assertThat(username.getValue()).isEqualTo(validUsername.toLowerCase().trim());
    }

    @Test
    @DisplayName("Should identify numeric only usernames correctly")
    void shouldIdentifyNumericOnlyUsernamesCorrectly() {
        // given
        Username numericOnly = Username.of("12345");
        Username alphaNumeric = Username.of("user123");
        Username alphabetic = Username.of("username");

        // when & then
        assertThat(numericOnly.isNumericOnly()).isTrue();
        assertThat(alphaNumeric.isNumericOnly()).isFalse();
        assertThat(alphabetic.isNumericOnly()).isFalse();
    }

    @Test
    @DisplayName("Should identify usernames with special characters correctly")
    void shouldIdentifyUsernamesWithSpecialCharactersCorrectly() {
        // given
        Username withUnderscore = Username.of("user_name");
        Username withHyphen = Username.of("user-name");
        Username withDot = Username.of("user.name");
        Username withMultiple = Username.of("user_name-test.123");
        Username withoutSpecial = Username.of("username123");

        // when & then
        assertThat(withUnderscore.hasSpecialCharacters()).isTrue();
        assertThat(withHyphen.hasSpecialCharacters()).isTrue();
        assertThat(withDot.hasSpecialCharacters()).isTrue();
        assertThat(withMultiple.hasSpecialCharacters()).isTrue();
        assertThat(withoutSpecial.hasSpecialCharacters()).isFalse();
    }

    @Test
    @DisplayName("Should implement equals and hashCode correctly")
    void shouldImplementEqualsAndHashCodeCorrectly() {
        // given
        Username username1 = Username.of("user123");
        Username username2 = Username.of("USER123");
        Username username3 = Username.of("other123");

        // when & then
        assertThat(username1).isEqualTo(username2); // case insensitive
        assertThat(username1).isNotEqualTo(username3);
        assertThat(username1.hashCode()).isEqualTo(username2.hashCode());
        assertThat(username1.hashCode()).isNotEqualTo(username3.hashCode());

        // Test against null and different class
        assertThat(username1).isNotEqualTo(null);
        assertThat(username1).isNotEqualTo("user123");
        
        // Test same instance
        assertThat(username1).isEqualTo(username1);
    }

    @Test
    @DisplayName("Should handle all allowed special characters correctly")
    void shouldHandleAllowedSpecialCharactersCorrectly() {
        // given
        Username underscoreUsername = Username.of("user_name");
        Username hyphenUsername = Username.of("user-name");
        Username dotUsername = Username.of("user.name");
        Username combinedUsername = Username.of("user_name-test.123");

        // when & then
        assertThat(underscoreUsername.getValue()).isEqualTo("user_name");
        assertThat(hyphenUsername.getValue()).isEqualTo("user-name");
        assertThat(dotUsername.getValue()).isEqualTo("user.name");
        assertThat(combinedUsername.getValue()).isEqualTo("user_name-test.123");
    }

    @Test
    @DisplayName("Should handle minimum and maximum valid lengths")
    void shouldHandleMinAndMaxValidLengths() {
        // given
        String minUsername = "abc"; // 3 characters
        String maxUsername = "a".repeat(50); // 50 characters

        // when
        Username minUsernameObj = Username.of(minUsername);
        Username maxUsernameObj = Username.of(maxUsername);

        // then
        assertThat(minUsernameObj.getValue()).isEqualTo("abc");
        assertThat(minUsernameObj.getValue()).hasSize(3);
        assertThat(maxUsernameObj.getValue()).hasSize(50);
    }

    @Test
    @DisplayName("Should handle edge cases for special character detection")
    void shouldHandleEdgeCasesForSpecialCharacterDetection() {
        // given
        Username onlyLetters = Username.of("username");
        Username onlyNumbers = Username.of("123456");
        Username lettersAndNumbers = Username.of("user123");
        Username singleSpecial = Username.of("user_1");
        Username multipleSpecials = Username.of("a_b-c.d");

        // when & then
        assertThat(onlyLetters.hasSpecialCharacters()).isFalse();
        assertThat(onlyNumbers.hasSpecialCharacters()).isFalse();
        assertThat(lettersAndNumbers.hasSpecialCharacters()).isFalse();
        assertThat(singleSpecial.hasSpecialCharacters()).isTrue();
        assertThat(multipleSpecials.hasSpecialCharacters()).isTrue();
    }

    @Test
    @DisplayName("Should handle edge cases for numeric only detection")
    void shouldHandleEdgeCasesForNumericOnlyDetection() {
        // given
        Username pureNumeric = Username.of("987654321");
        Username numericWithSpecial = Username.of("123_456");
        Username numericWithLetter = Username.of("123a");
        Username singleNumber = Username.of("555"); // Changed to valid length
        Username mixedContent = Username.of("a1b2c3");

        // when & then
        assertThat(pureNumeric.isNumericOnly()).isTrue();
        assertThat(numericWithSpecial.isNumericOnly()).isFalse();
        assertThat(numericWithLetter.isNumericOnly()).isFalse();
        assertThat(singleNumber.isNumericOnly()).isTrue();
        assertThat(mixedContent.isNumericOnly()).isFalse();
    }

    @Test
    @DisplayName("Should handle username starting and ending with valid characters")
    void shouldHandleUsernameStartingAndEndingWithValidCharacters() {
        // given & when & then
        assertThat(Username.of("a123b").getValue()).isEqualTo("a123b"); // letter-number-letter
        assertThat(Username.of("1abc2").getValue()).isEqualTo("1abc2"); // number-letter-number
        assertThat(Username.of("abc").getValue()).isEqualTo("abc"); // all letters
        assertThat(Username.of("123").getValue()).isEqualTo("123"); // all numbers
        assertThat(Username.of("a_b").getValue()).isEqualTo("a_b"); // letter-special-letter
        assertThat(Username.of("1_2").getValue()).isEqualTo("1_2"); // number-special-number
    }

    @Test
    @DisplayName("Should maintain case sensitivity in comparison with original input")
    void shouldMaintainProperCaseHandling() {
        // given
        String originalInput = "TestUser123";
        
        // when
        Username username = Username.of(originalInput);
        
        // then
        assertThat(username.getValue()).isEqualTo("testuser123");
        assertThat(username).isEqualTo(Username.of("TESTUSER123"));
        assertThat(username).isEqualTo(Username.of("testuser123"));
    }
}