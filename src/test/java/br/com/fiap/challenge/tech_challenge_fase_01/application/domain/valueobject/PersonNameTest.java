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

@DisplayName("PersonName Value Object Tests")
class PersonNameTest {

    @Test
    @DisplayName("Should create valid person name")
    void shouldCreateValidPersonName() {
        // given
        String nameValue = "João Silva";

        // when
        PersonName personName = PersonName.of(nameValue);

        // then
        assertThat(personName.getValue()).isEqualTo("João Silva");
        assertThat(personName.toString()).isEqualTo("João Silva");
    }

    @Test
    @DisplayName("Should normalize name by trimming spaces")
    void shouldNormalizeNameByTrimmingSpaces() {
        // given
        String nameWithSpaces = "  Maria dos Santos  ";

        // when
        PersonName personName = PersonName.of(nameWithSpaces);

        // then
        assertThat(personName.getValue()).isEqualTo("Maria dos Santos");
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {" ", "\t", "\n", "   "})
    @DisplayName("Should throw exception for null, empty or blank name")
    void shouldThrowExceptionForInvalidName(String invalidName) {
        // when & then
        assertThatThrownBy(() -> PersonName.of(invalidName))
                .isInstanceOf(InvalidFieldException.class)
                .hasFieldOrPropertyWithValue("fieldName", "name")
                .hasMessageContaining("Nome é obrigatório");
    }

    @Test
    @DisplayName("Should throw exception for name too short")
    void shouldThrowExceptionForNameTooShort() {
        // given
        String shortName = "A";

        // when & then
        assertThatThrownBy(() -> PersonName.of(shortName))
                .isInstanceOf(InvalidFieldException.class)
                .hasFieldOrPropertyWithValue("fieldName", "name")
                .hasMessageContaining("Nome deve ter no mínimo 2 caracteres");
    }

    @Test
    @DisplayName("Should throw exception for name too long")
    void shouldThrowExceptionForNameTooLong() {
        // given
        String longName = "A".repeat(101);

        // when & then
        assertThatThrownBy(() -> PersonName.of(longName))
                .isInstanceOf(InvalidFieldException.class)
                .hasFieldOrPropertyWithValue("fieldName", "name")
                .hasMessageContaining("Nome deve ter no máximo 100 caracteres");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "João123", "Maria@Silva", "Pedro#Santos", 
            "Ana&Costa", "Lucas+Lima", "Carla*Souza",
            "José=Oliveira", "Fernanda|Rocha"
    })
    @DisplayName("Should throw exception for name with invalid characters")
    void shouldThrowExceptionForNameWithInvalidCharacters(String invalidName) {
        // when & then
        assertThatThrownBy(() -> PersonName.of(invalidName))
                .isInstanceOf(InvalidFieldException.class)
                .hasFieldOrPropertyWithValue("fieldName", "name")
                .hasMessageContaining("Nome deve conter apenas letras, espaços e caracteres especiais permitidos");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "José", "María", "João-Paulo", "Ana Paula", 
            "O'Connor", "D'Angelo", "Saint-Exupéry", "Mary-Jane",
            "José da Silva", "Ana Cristina", "Jean-Luc", "Marie-Claire",
            "Ângela", "François", "José María", "São João"
    })
    @DisplayName("Should accept valid name patterns")
    void shouldAcceptValidNamePatterns(String validName) {
        // when
        PersonName personName = PersonName.of(validName);

        // then
        assertThat(personName.getValue()).isEqualTo(validName);
    }

    @Test
    @DisplayName("Should get first name correctly")
    void shouldGetFirstNameCorrectly() {
        // given
        PersonName singleName = PersonName.of("João");
        PersonName multipleName = PersonName.of("Maria dos Santos Silva");

        // when & then
        assertThat(singleName.getFirstName()).isEqualTo("João");
        assertThat(multipleName.getFirstName()).isEqualTo("Maria");
    }

    @Test
    @DisplayName("Should get last name correctly")
    void shouldGetLastNameCorrectly() {
        // given
        PersonName singleName = PersonName.of("João");
        PersonName multipleName = PersonName.of("Maria dos Santos Silva");

        // when & then
        assertThat(singleName.getLastName()).isEmpty();
        assertThat(multipleName.getLastName()).isEqualTo("Silva");
    }

    @Test
    @DisplayName("Should get initials correctly")
    void shouldGetInitialsCorrectly() {
        // given
        PersonName singleName = PersonName.of("joão");
        PersonName multipleName = PersonName.of("maria dos santos silva");
        PersonName withSpaces = PersonName.of("ana   paula   oliveira");

        // when & then
        assertThat(singleName.getInitials()).isEqualTo("J");
        assertThat(multipleName.getInitials()).isEqualTo("MDSS");
        assertThat(withSpaces.getInitials()).isEqualTo("APO");
    }

    @Test
    @DisplayName("Should identify compound names correctly")
    void shouldIdentifyCompoundNamesCorrectly() {
        // given
        PersonName singleName = PersonName.of("João");
        PersonName compoundName = PersonName.of("Ana Paula");

        // when & then
        assertThat(singleName.isCompound()).isFalse();
        assertThat(compoundName.isCompound()).isTrue();
    }

    @Test
    @DisplayName("Should implement equals and hashCode correctly")
    void shouldImplementEqualsAndHashCodeCorrectly() {
        // given
        PersonName name1 = PersonName.of("João Silva");
        PersonName name2 = PersonName.of("João Silva");
        PersonName name3 = PersonName.of("Maria Santos");

        // when & then
        assertThat(name1).isEqualTo(name2);
        assertThat(name1).isNotEqualTo(name3);
        assertThat(name1.hashCode()).isEqualTo(name2.hashCode());
        assertThat(name1.hashCode()).isNotEqualTo(name3.hashCode());

        // Test against null and different class
        assertThat(name1).isNotEqualTo(null);
        assertThat(name1).isNotEqualTo("João Silva");
        
        // Test same instance
        assertThat(name1).isEqualTo(name1);
    }

    @Test
    @DisplayName("Should handle names with accents and special characters correctly")
    void shouldHandleAccentsAndSpecialCharacters() {
        // given
        String nameWithAccents = "José María Ángel";
        String nameWithApostrophe = "O'Connor";
        String nameWithHyphen = "Jean-Luc";
        String nameWithPeriod = "Dr. João Silva";

        // when
        PersonName accentName = PersonName.of(nameWithAccents);
        PersonName apostropheName = PersonName.of(nameWithApostrophe);
        PersonName hyphenName = PersonName.of(nameWithHyphen);
        PersonName periodName = PersonName.of(nameWithPeriod);

        // then
        assertThat(accentName.getValue()).isEqualTo("José María Ángel");
        assertThat(apostropheName.getValue()).isEqualTo("O'Connor");
        assertThat(hyphenName.getValue()).isEqualTo("Jean-Luc");
        assertThat(periodName.getValue()).isEqualTo("Dr. João Silva");
    }

    @Test
    @DisplayName("Should handle edge cases for name methods")
    void shouldHandleEdgeCasesForNameMethods() {
        // given
        PersonName emptyPartName = PersonName.of("João  Silva"); // multiple spaces

        // when & then
        assertThat(emptyPartName.getFirstName()).isEqualTo("João");
        assertThat(emptyPartName.getLastName()).isEqualTo("Silva");
        assertThat(emptyPartName.getInitials()).isEqualTo("JS");
        assertThat(emptyPartName.isCompound()).isTrue();
    }

    @Test
    @DisplayName("Should accept minimum and maximum valid lengths")
    void shouldAcceptMinAndMaxValidLengths() {
        // given
        String minName = "Ab"; // 2 characters
        String maxName = "A".repeat(100); // 100 characters

        // when
        PersonName minPersonName = PersonName.of(minName);
        PersonName maxPersonName = PersonName.of(maxName);

        // then
        assertThat(minPersonName.getValue()).isEqualTo("Ab");
        assertThat(maxPersonName.getValue()).hasSize(100);
    }
}