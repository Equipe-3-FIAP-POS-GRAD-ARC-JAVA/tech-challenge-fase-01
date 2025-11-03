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

@DisplayName("Email Value Object Tests")
class EmailTest {

    @Test
    @DisplayName("Should create valid email")
    void shouldCreateValidEmail() {
        // given
        String emailValue = "user@example.com";

        // when
        Email email = Email.of(emailValue);

        // then
        assertThat(email.getValue()).isEqualTo("user@example.com");
        assertThat(email.toString()).isEqualTo("user@example.com");
    }

    @Test
    @DisplayName("Should normalize email by converting to lowercase")
    void shouldNormalizeEmailByConvertingToLowercase() {
        // given
        String upperCaseEmail = "USER@EXAMPLE.COM";
        String mixedCaseEmail = "UsEr@ExAmPlE.CoM";

        // when
        Email email1 = Email.of(upperCaseEmail);
        Email email2 = Email.of(mixedCaseEmail);

        // then
        assertThat(email1.getValue()).isEqualTo("user@example.com");
        assertThat(email2.getValue()).isEqualTo("user@example.com");
    }

    @Test
    @DisplayName("Should trim spaces from email")
    void shouldTrimSpacesFromEmail() {
        // given
        String emailWithSpaces = "  user@example.com  ";

        // when
        Email email = Email.of(emailWithSpaces);

        // then
        assertThat(email.getValue()).isEqualTo("user@example.com");
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {" ", "\t", "\n", "   "})
    @DisplayName("Should throw exception for null, empty or blank email")
    void shouldThrowExceptionForInvalidEmail(String invalidEmail) {
        // when & then
        assertThatThrownBy(() -> Email.of(invalidEmail))
                .isInstanceOf(InvalidFieldException.class)
                .hasFieldOrPropertyWithValue("fieldName", "email")
                .hasMessageContaining("Email é obrigatório");
    }

    @Test
    @DisplayName("Should throw exception for email too long")
    void shouldThrowExceptionForEmailTooLong() {
        // given
        String longEmail = "a".repeat(250) + "@example.com";

        // when & then
        assertThatThrownBy(() -> Email.of(longEmail))
                .isInstanceOf(InvalidFieldException.class)
                .hasFieldOrPropertyWithValue("fieldName", "email")
                .hasMessageContaining("Email deve ter no máximo 255 caracteres");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "invalid-email",
            "user@",
            "@example.com",
            "user.example.com",
            "user@@example.com",
            "user name@example.com",
            "user@exam ple.com"
    })
    @DisplayName("Should throw exception for invalid email format")
    void shouldThrowExceptionForInvalidEmailFormat(String invalidEmail) {
        // when & then
        assertThatThrownBy(() -> Email.of(invalidEmail))
                .isInstanceOf(InvalidFieldException.class)
                .hasFieldOrPropertyWithValue("fieldName", "email")
                .hasMessageContaining("Email inválido");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "user@example.com",
            "test.email@domain.co.uk",
            "user+tag@example.org",
            "user_name@example-domain.com",
            "123@example.com",
            "user@sub.domain.com",
            "a@b.co",
            "very.long.email.address@very.long.domain.name.com",
            "user123@example123.com",
            "user-name@example-domain.com"
    })
    @DisplayName("Should accept valid email formats")
    void shouldAcceptValidEmailFormats(String validEmail) {
        // when
        Email email = Email.of(validEmail);

        // then
        assertThat(email.getValue()).isEqualTo(validEmail.toLowerCase());
    }

    @Test
    @DisplayName("Should get local part correctly")
    void shouldGetLocalPartCorrectly() {
        // given
        Email simpleEmail = Email.of("user@example.com");
        Email complexEmail = Email.of("user.name+tag@domain.co.uk");

        // when & then
        assertThat(simpleEmail.getLocalPart()).isEqualTo("user");
        assertThat(complexEmail.getLocalPart()).isEqualTo("user.name+tag");
    }

    @Test
    @DisplayName("Should get domain correctly")
    void shouldGetDomainCorrectly() {
        // given
        Email simpleEmail = Email.of("user@example.com");
        Email complexEmail = Email.of("user@sub.domain.co.uk");

        // when & then
        assertThat(simpleEmail.getDomain()).isEqualTo("example.com");
        assertThat(complexEmail.getDomain()).isEqualTo("sub.domain.co.uk");
    }

    @Test
    @DisplayName("Should check domain belonging correctly")
    void shouldCheckDomainBelongingCorrectly() {
        // given
        Email email = Email.of("user@example.com");

        // when & then
        assertThat(email.belongsToDomain("example.com")).isTrue();
        assertThat(email.belongsToDomain("EXAMPLE.COM")).isTrue(); // case insensitive
        assertThat(email.belongsToDomain("other.com")).isFalse();
        assertThat(email.belongsToDomain("sub.example.com")).isFalse();
        assertThat(email.belongsToDomain("example.co")).isFalse();
    }

    @Test
    @DisplayName("Should implement equals and hashCode correctly")
    void shouldImplementEqualsAndHashCodeCorrectly() {
        // given
        Email email1 = Email.of("user@example.com");
        Email email2 = Email.of("USER@EXAMPLE.COM");
        Email email3 = Email.of("other@example.com");

        // when & then
        assertThat(email1).isEqualTo(email2); // case insensitive
        assertThat(email1).isNotEqualTo(email3);
        assertThat(email1.hashCode()).isEqualTo(email2.hashCode());
        assertThat(email1.hashCode()).isNotEqualTo(email3.hashCode());

        // Test against null and different class
        assertThat(email1).isNotEqualTo(null);
        assertThat(email1).isNotEqualTo("user@example.com");
        
        // Test same instance
        assertThat(email1).isEqualTo(email1);
    }

    @Test
    @DisplayName("Should handle special characters in email correctly")
    void shouldHandleSpecialCharactersCorrectly() {
        // given
        String emailWithPlus = "user+tag@example.com";
        String emailWithUnderscore = "user_name@example.com";
        String emailWithDots = "user.name@example.com";
        String emailWithHyphen = "user-name@example-domain.com";

        // when
        Email plusEmail = Email.of(emailWithPlus);
        Email underscoreEmail = Email.of(emailWithUnderscore);
        Email dotsEmail = Email.of(emailWithDots);
        Email hyphenEmail = Email.of(emailWithHyphen);

        // then
        assertThat(plusEmail.getValue()).isEqualTo("user+tag@example.com");
        assertThat(underscoreEmail.getValue()).isEqualTo("user_name@example.com");
        assertThat(dotsEmail.getValue()).isEqualTo("user.name@example.com");
        assertThat(hyphenEmail.getValue()).isEqualTo("user-name@example-domain.com");
    }

    @Test
    @DisplayName("Should handle subdomains correctly")
    void shouldHandleSubdomainsCorrectly() {
        // given
        Email subdomainEmail = Email.of("user@mail.example.com");

        // when & then
        assertThat(subdomainEmail.getDomain()).isEqualTo("mail.example.com");
        assertThat(subdomainEmail.belongsToDomain("mail.example.com")).isTrue();
        assertThat(subdomainEmail.belongsToDomain("example.com")).isFalse();
    }

    @Test
    @DisplayName("Should handle numeric domains correctly")
    void shouldHandleNumericDomainsCorrectly() {
        // given
        Email numericEmail = Email.of("user@123.456.789.com");

        // when & then
        assertThat(numericEmail.getDomain()).isEqualTo("123.456.789.com");
        assertThat(numericEmail.belongsToDomain("123.456.789.com")).isTrue();
    }

    @Test
    @DisplayName("Should handle international domains correctly")
    void shouldHandleInternationalDomainsCorrectly() {
        // given
        Email intlEmail = Email.of("user@example.co.uk");

        // when & then
        assertThat(intlEmail.getDomain()).isEqualTo("example.co.uk");
        assertThat(intlEmail.belongsToDomain("example.co.uk")).isTrue();
        assertThat(intlEmail.belongsToDomain("co.uk")).isFalse();
    }

    @Test
    @DisplayName("Should accept maximum valid length")
    void shouldAcceptMaximumValidLength() {
        // given - create email with exactly 255 characters
        String localPart = "a".repeat(64);
        String domain = "b".repeat(186) + ".com"; // total = 64 + 1 + 190 = 255
        String maxEmail = localPart + "@" + domain;

        // when
        Email email = Email.of(maxEmail);

        // then
        assertThat(email.getValue()).hasSize(255);
        assertThat(email.getLocalPart()).isEqualTo(localPart);
        assertThat(email.getDomain()).isEqualTo(domain);
    }

    @Test
    @DisplayName("Should handle edge cases for domain checking")
    void shouldHandleEdgeCasesForDomainChecking() {
        // given
        Email email = Email.of("user@example.com");

        // when & then
        assertThat(email.belongsToDomain(null)).isFalse();
        assertThat(email.belongsToDomain("")).isFalse();
        assertThat(email.belongsToDomain("   ")).isFalse();
    }
}