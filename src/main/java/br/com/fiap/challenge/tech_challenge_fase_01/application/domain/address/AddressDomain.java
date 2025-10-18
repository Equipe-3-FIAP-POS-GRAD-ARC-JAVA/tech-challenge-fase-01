package br.com.fiap.challenge.tech_challenge_fase_01.application.domain.address;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.BusinessRuleException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.InvalidFieldException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Email;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.PersonName;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Username;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressDomain {

    private static final int STREET_MIN = 3, STREET_MAX = 100;
    private static final int NUMBER_MIN = 1, NUMBER_MAX = 20;
    private static final int CITY_MIN = 2, CITY_MAX = 50;

    private UUID id;
    private UUID userId;
    private String street;
    private String number;
    private String city;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AddressDomain create(UUID userId, String street, String number, String city) {
        validate(userId, street, number, city);
        return AddressDomain.builder()
                .userId(userId)
                .street(street.trim())
                .number(number.trim())
                .city(city.trim())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private static void validate(UUID userId, String street, String number, String city) {
        if (userId == null) throw new InvalidFieldException("userId", "Usuário é obrigatório");
        checkLen("street", street, STREET_MIN, STREET_MAX, "Rua");
        checkLen("number", number, NUMBER_MIN, NUMBER_MAX, "Número");
        checkLen("city", city, CITY_MIN, CITY_MAX, "Cidade");
    }

    private static void checkLen(String field, String value, int min, int max, String label) {
        if (value == null || value.isBlank())
            throw new InvalidFieldException(field, label + " é obrigatório");
        var v = value.trim();
        if (v.length() < min) throw new InvalidFieldException(field, label + " deve ter no mínimo " + min + " caracteres");
        if (v.length() > max) throw new InvalidFieldException(field, label + " deve ter no máximo " + max + " caracteres");
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressDomain that)) return false;
        return Objects.equals(id, that.id);
    }
    @Override public int hashCode() { return Objects.hash(id); }

    @Override public String toString() {
        return "AddressDomain{id=" + id + ", userId=" + userId + ", street='" + street + "', number='" + number + "', city='" + city + "'}";
    }

    public void updateInfo(String street, String number, String city) {
        this.street = street;
        this.number = number;
        this.city = city;
        this.updatedAt = LocalDateTime.now();
    }
}
